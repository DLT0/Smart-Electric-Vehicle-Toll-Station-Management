/**
 * Smart EV Toll Station Management – Lâm Đồng
 * Frontend App Logic (app.js)
 * 
 * Chạy độc lập với mock data khi backend Java chưa khởi động.
 * Khi backend Java (ApiServer.java) chạy tại localhost:8080,
 * uncomment FLAG_USE_API = true để kết nối thật.
 */

'use strict';

// ============================================================
// CONFIG
// ============================================================
const CONFIG = {
    API_BASE: 'http://localhost:8080/api',
    REFRESH_INTERVAL: 10000, // 10 giây
    ROWS_PER_PAGE: 8,
    MAINTENANCE_LIMIT: 500,  // giờ
};

// Đổi sang TRUE khi backend Java đã chạy
const FLAG_USE_API = false;

// ============================================================
// MOCK DATA (dữ liệu mẫu từ Module.java khoiTaoDuLieuMau())
// ============================================================
const MOCK_DATA = [
    {
        maTram: 'T001', loai: 'TramSacCham',
        tenTram: 'Tram Sac Cham Da Lat 1',
        viTri: 'Da Lat', congSuat: 7.2,
        trangThai: true, thoiGianHoatDong: 120.5
    },
    {
        maTram: 'T002', loai: 'TramSacCham',
        tenTram: 'Tram Sac Cham Da Lat 2',
        viTri: 'Da Lat', congSuat: 11.0,
        trangThai: true, thoiGianHoatDong: 45.0
    },
    {
        maTram: 'T003', loai: 'TramSacNhanh',
        tenTram: 'Tram Sac Nhanh Duc Trong 1',
        viTri: 'Duc Trong', congSuat: 30.0,
        trangThai: false, thoiGianHoatDong: 280.0
    },
    {
        maTram: 'T004', loai: 'TramSacNhanh',
        tenTram: 'Tram Sac Nhanh Bao Loc 1',
        viTri: 'Bao Loc', congSuat: 60.0,
        trangThai: true, thoiGianHoatDong: 390.0
    },
    {
        maTram: 'T005', loai: 'TramSacSieuNhanh',
        tenTram: 'Tram Sac Sieu Nhanh Di Linh 1',
        viTri: 'Di Linh', congSuat: 150.0,
        trangThai: true, thoiGianHoatDong: 450.0
    },
    {
        maTram: 'T006', loai: 'TramSacSieuNhanh',
        tenTram: 'Tram Sac Sieu Nhanh Da Lat 3',
        viTri: 'Da Lat', congSuat: 250.0,
        trangThai: false, thoiGianHoatDong: 78.0
    },
];

// ============================================================
// APP STATE
// ============================================================
const state = {
    stations: [],
    filtered: [],
    currentView: 'card',    // 'card' | 'table'
    activePage: 'dashboard',
    filter: {
        query: '',
        status: 'all',
        type: 'all',
        viTri: 'all',
    },
    tablePage: 1,
    sortCol: null,
    sortDir: 'asc',
    editingId: null,
    refreshTimer: null,
};

// ============================================================
// HELPERS
// ============================================================
const GIA_MOI_KWH = 3850;

function getStationType(loai) {
    if (loai === 'TramSacCham') return { label: 'Sac Cham', cls: 'type-cham', power: '7–11 kW' };
    if (loai === 'TramSacNhanh') return { label: 'Sac Nhanh', cls: 'type-nhanh', power: '12–120 kW' };
    if (loai === 'TramSacSieuNhanh') return { label: 'Sieu Nhanh', cls: 'type-sieunhanh', power: '121–300 kW' };
    return { label: 'Unknown', cls: '', power: '' };
}

function classifyByPower(kw) {
    if (kw <= 11) return 'TramSacCham';
    if (kw <= 120) return 'TramSacNhanh';
    return 'TramSacSieuNhanh';
}

function buildTenTram(loai, viTri, stt) {
    if (loai === 'TramSacCham') return `Tram Sac Cham ${viTri} ${stt}`;
    if (loai === 'TramSacNhanh') return `Tram Sac Nhanh ${viTri} ${stt}`;
    if (loai === 'TramSacSieuNhanh') return `Tram Sac Sieu Nhanh ${viTri} ${stt}`;
    return `Tram Sac ${viTri} ${stt}`;
}

function isMaintenance(s) {
    return s.thoiGianHoatDong >= CONFIG.MAINTENANCE_LIMIT;
}

function isWarning(s) {
    return s.thoiGianHoatDong >= CONFIG.MAINTENANCE_LIMIT * 0.8 && !isMaintenance(s);
}

function getStationStatus(s) {
    if (isMaintenance(s)) return 'maintain';
    if (!s.trangThai) return 'charging';
    return 'ready';
}

function getWearPct(s) {
    return Math.min((s.thoiGianHoatDong / CONFIG.MAINTENANCE_LIMIT) * 100, 100);
}

function getWearClass(pct) {
    if (pct >= 90) return 'wear-high';
    if (pct >= 70) return 'wear-medium';
    return 'wear-low';
}

function formatCost(kw) {
    return Math.round(kw * GIA_MOI_KWH).toLocaleString('vi-VN') + ' VND/h';
}

function formatHours(h) {
    return h.toFixed(1) + ' h';
}

function uniqueId() {
    return 'st' + Date.now().toString(36) + Math.random().toString(36).slice(2, 5);
}

// ============================================================
// API / DATA LAYER
// ============================================================
async function fetchStations() {
    if (FLAG_USE_API) {
        try {
            const res = await fetch(`${CONFIG.API_BASE}/stations`);
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            state.stations = await res.json();
        } catch (err) {
            showToast('Khong ket noi duoc toi Backend Java. Su dung du lieu local.', 'warning');
            console.warn('API error:', err);
            if (state.stations.length === 0) state.stations = [...MOCK_DATA];
        }
    } else {
        // Dùng mock data
        state.stations = state.stations.length > 0 ? state.stations : [...MOCK_DATA];
    }
    applyFilter();
    renderAll();
}

async function apiAddStation(data) {
    if (FLAG_USE_API) {
        const res = await fetch(`${CONFIG.API_BASE}/stations`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data),
        });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return await res.json();
    } else {
        // Mock: thêm thẳng vào state
        state.stations.push(data);
        return data;
    }
}

async function apiUpdateStatus(id, updates) {
    if (FLAG_USE_API) {
        const res = await fetch(`${CONFIG.API_BASE}/stations/${id}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updates),
        });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
    } else {
        const s = state.stations.find(x => x.maTram === id);
        if (s) Object.assign(s, updates);
    }
}

async function apiDeleteStation(id) {
    if (FLAG_USE_API) {
        const res = await fetch(`${CONFIG.API_BASE}/stations/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
    } else {
        state.stations = state.stations.filter(x => x.maTram !== id);
    }
}

// ============================================================
// FILTER LOGIC
// ============================================================
function applyFilter() {
    const { query, status, type, viTri } = state.filter;
    state.filtered = state.stations.filter(s => {
        const q = query.toLowerCase();
        if (q && !s.maTram.toLowerCase().includes(q) && !s.tenTram.toLowerCase().includes(q)) return false;

        if (status === 'ready' && getStationStatus(s) !== 'ready') return false;
        if (status === 'charging' && getStationStatus(s) !== 'charging') return false;
        if (status === 'maintain' && getStationStatus(s) !== 'maintain') return false;

        if (type !== 'all' && s.loai !== type) return false;
        if (viTri !== 'all' && s.viTri !== viTri) return false;
        return true;
    });
    state.tablePage = 1;
    updateResultsCount();
}

function updateResultsCount() {
    const el = document.getElementById('results-count');
    if (el) el.textContent = `Tim thay ${state.filtered.length} / ${state.stations.length} tram`;
}

// ============================================================
// RENDER – STATS CARDS
// ============================================================
function renderStats() {
    const all = state.stations;
    const ready = all.filter(s => getStationStatus(s) === 'ready');
    const charging = all.filter(s => getStationStatus(s) === 'charging');
    const maintain = all.filter(s => getStationStatus(s) === 'maintain');
    const totalKW = charging.reduce((sum, s) => sum + s.congSuat, 0);

    setInner('stat-total', all.length);
    setInner('stat-ready', ready.length);
    setInner('stat-charging', charging.length);
    setInner('stat-power', totalKW.toFixed(1) + ' kW');
    setInner('stat-maintain', maintain.length);

    // Sub labels
    setInner('stat-total-sub', `${ready.length} san sang · ${charging.length} hoat dong`);
    setInner('stat-ready-sub', ready.length > 0 ? 'San sang nhan xe' : 'Khong co tram trong');
    setInner('stat-charging-sub', charging.length > 0 ? `${charging.length} xe dang sac` : 'Khong co xe sac');
    setInner('stat-power-sub', `Tong cong suat dang dung`);
}

// ============================================================
// RENDER – MAINTENANCE ALERT
// ============================================================
function renderAlerts() {
    const container = document.getElementById('alert-container');
    if (!container) return;
    const overdue = state.stations.filter(s => s.thoiGianHoatDong >= CONFIG.MAINTENANCE_LIMIT);
    const neardue = state.stations.filter(s => isWarning(s));
    let html = '';
    if (overdue.length > 0) {
        html += `<div class="alert-banner alert-danger">
      ⚠️ <strong>${overdue.length} tram</strong> da vuot han bao tri (≥${CONFIG.MAINTENANCE_LIMIT}h): 
      ${overdue.map(s => `<strong>${s.maTram}</strong>`).join(', ')}
    </div>`;
    }
    if (neardue.length > 0) {
        html += `<div class="alert-banner alert-warning">
      🔔 <strong>${neardue.length} tram</strong> sap den han bao tri: 
      ${neardue.map(s => `<strong>${s.maTram}</strong>`).join(', ')}
    </div>`;
    }
    container.innerHTML = html;
}

// ============================================================
// RENDER – STATION CARDS
// ============================================================
function renderCards() {
    const grid = document.getElementById('cards-grid');
    if (!grid) return;

    if (state.filtered.length === 0) {
        grid.innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">🔌</div>
        <p>Khong tim thay tram sac nao phu hop voi bo loc.</p>
        <button class="btn btn-ghost btn-sm" onclick="resetFilter()">Xoa bo loc</button>
      </div>`;
        return;
    }

    grid.innerHTML = state.filtered.map(s => buildStationCard(s)).join('');

    // Add animations with staggered delay
    grid.querySelectorAll('.station-card').forEach((card, i) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(16px)';
        setTimeout(() => {
            card.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, i * 60);
    });
}

function buildStationCard(s) {
    const statusKey = getStationStatus(s);
    const type = getStationType(s.loai);
    const wearPct = getWearPct(s);
    const wearClass = getWearClass(wearPct);

    const statusLabels = { ready: 'San sang', charging: 'Dang sac', maintain: 'Bao tri' };
    const statusLabel = statusLabels[statusKey];

    return `
  <div class="station-card status-${statusKey}" id="card-${s.maTram}">
    <div class="station-card-header">
      <div class="station-card-badges">
        <span class="type-badge ${type.cls}">${type.label}</span>
      </div>
      <span class="station-id">${s.maTram}</span>
    </div>
    <div class="station-card-body">
      <div class="station-name">⚡ ${s.tenTram}</div>
      <div class="station-meta">
        <div class="station-meta-item">
          <span class="icon">📍</span>
          <span>${s.viTri}</span>
        </div>
        <div class="station-meta-item">
          <span class="icon">🔋</span>
          <span><strong>${s.congSuat} kW</strong></span>
        </div>
        <div class="station-meta-item">
          <span class="icon">⏱️</span>
          <span>Da van hanh: <strong>${formatHours(s.thoiGianHoatDong)}</strong></span>
        </div>
        <div class="station-meta-item">
          <span class="icon">💰</span>
          <span>${formatCost(s.congSuat)}</span>
        </div>
      </div>
    </div>

    <div class="maintenance-section">
      <div class="progress-label">
        <span>Do hao mon bao tri</span>
        <strong>${wearPct.toFixed(0)}% / ${CONFIG.MAINTENANCE_LIMIT}h</strong>
      </div>
      <div class="progress-bar-track" title="${s.thoiGianHoatDong}h / ${CONFIG.MAINTENANCE_LIMIT}h">
        <div class="progress-bar-fill ${wearClass}" style="width: ${wearPct}%"></div>
      </div>
    </div>

    <div class="status-section">
      <span class="status-badge ${statusKey}">${statusLabel}</span>
    </div>

    <div class="station-card-actions">
      <button class="btn btn-ghost btn-sm" onclick="openStatusModal('${s.maTram}')" title="Cap nhat trang thai">
        🔄 Doi TT
      </button>
      <button class="btn btn-danger btn-sm" onclick="confirmDelete('${s.maTram}')" title="Xoa tram">
        🗑 Xoa
      </button>
    </div>
  </div>`;
}

// ============================================================
// RENDER – TABLE VIEW
// ============================================================
function renderTable() {
    const container = document.getElementById('table-body');
    if (!container) return;

    const start = (state.tablePage - 1) * CONFIG.ROWS_PER_PAGE;
    const rows = state.filtered.slice(start, start + CONFIG.ROWS_PER_PAGE);

    if (rows.length === 0) {
        container.innerHTML = `<tr><td colspan="9" class="text-center" style="padding:2rem;color:var(--text-muted);">Khong co du lieu</td></tr>`;
        renderPagination();
        return;
    }

    const statusLabels = { ready: 'San sang', charging: 'Dang sac', maintain: 'Bao tri' };

    container.innerHTML = rows.map((s, idx) => {
        const statusKey = getStationStatus(s);
        const type = getStationType(s.loai);
        return `
    <tr>
      <td>${start + idx + 1}</td>
      <td class="td-id">${s.maTram}</td>
      <td class="td-name">${s.tenTram}</td>
      <td>${s.viTri}</td>
      <td><span class="type-badge ${type.cls}">${type.label}</span></td>
      <td class="td-power">${s.congSuat} kW</td>
      <td><span class="status-badge ${statusKey}">${statusLabels[statusKey]}</span></td>
      <td>${formatCost(s.congSuat)}</td>
      <td>
        <div class="table-actions">
          <button class="btn btn-ghost btn-sm" onclick="openStatusModal('${s.maTram}')">🔄</button>
          <button class="btn btn-danger btn-sm" onclick="confirmDelete('${s.maTram}')">🗑</button>
        </div>
      </td>
    </tr>`;
    }).join('');

    renderPagination();
}

function renderPagination() {
    const container = document.getElementById('pagination');
    if (!container) return;
    const total = Math.ceil(state.filtered.length / CONFIG.ROWS_PER_PAGE);
    if (total <= 1) { container.innerHTML = ''; return; }

    let html = `<button class="pagination-btn" onclick="goPage(${state.tablePage - 1})" ${state.tablePage <= 1 ? 'disabled' : ''}>‹</button>`;
    for (let i = 1; i <= total; i++) {
        html += `<button class="pagination-btn ${i === state.tablePage ? 'active' : ''}" onclick="goPage(${i})">${i}</button>`;
    }
    html += `<button class="pagination-btn" onclick="goPage(${state.tablePage + 1})" ${state.tablePage >= total ? 'disabled' : ''}>›</button>`;
    container.innerHTML = html;
}

function goPage(p) {
    const total = Math.ceil(state.filtered.length / CONFIG.ROWS_PER_PAGE);
    if (p < 1 || p > total) return;
    state.tablePage = p;
    renderTable();
}

// ============================================================
// RENDER – ALL
// ============================================================
function renderAll() {
    renderStats();
    renderAlerts();
    renderCards();
    renderTable();
    populateViTriSelect();
}

// ============================================================
// VIEW TOGGLE (Card ↔ Table)
// ============================================================
function switchView(view) {
    state.currentView = view;
    const cardView = document.getElementById('card-view');
    const tableView = document.getElementById('table-view');
    const btnCard = document.getElementById('btn-view-card');
    const btnTable = document.getElementById('btn-view-table');

    if (view === 'card') {
        cardView.classList.remove('hidden');
        tableView.classList.add('hidden');
        btnCard.classList.add('active');
        btnTable.classList.remove('active');
    } else {
        cardView.classList.add('hidden');
        tableView.classList.remove('hidden');
        btnCard.classList.remove('active');
        btnTable.classList.add('active');
        renderTable();
    }
}

// ============================================================
// NAVIGATION
// ============================================================
function navigate(page) {
    state.activePage = page;
    document.querySelectorAll('.page-section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));

    const section = document.getElementById(`page-${page}`);
    if (section) section.classList.add('active');
    const navItem = document.getElementById(`nav-${page}`);
    if (navItem) navItem.classList.add('active');

    // Update header title
    const titles = {
        dashboard: '📊 Dashboard',
        list: '📋 Danh sach Tram Sac',
        add: '➕ Them Tram Moi',
    };
    setInner('header-title-text', titles[page] || 'Dashboard');

    // Close mobile sidebar
    document.getElementById('sidebar')?.classList.remove('open');
}

// ============================================================
// FILTER INTERACTION
// ============================================================
function onSearch(e) {
    state.filter.query = e.target.value.trim();
    applyFilter();
    renderCards();
    renderTable();
}

function filterStatus(status) {
    state.filter.status = status;
    document.querySelectorAll('.filter-chip[data-filter="status"]').forEach(c => {
        c.classList.toggle('active', c.dataset.value === status);
        // Apply charging color for charging chip
        if (c.dataset.value === 'charging') c.classList.toggle('chip-charging', c.dataset.value === status);
        if (c.dataset.value === 'maintain') c.classList.toggle('chip-maintain', c.dataset.value === status);
    });
    applyFilter(); renderCards(); renderTable();
}

function filterType(type) {
    state.filter.type = type;
    document.querySelectorAll('.filter-chip[data-filter="type"]').forEach(c => {
        c.classList.toggle('active', c.dataset.value === type);
    });
    applyFilter(); renderCards(); renderTable();
}

function onViTriChange(e) {
    state.filter.viTri = e.target.value;
    applyFilter(); renderCards(); renderTable();
}

function resetFilter() {
    state.filter = { query: '', status: 'all', type: 'all', viTri: 'all' };
    document.getElementById('search-input').value = '';
    document.querySelectorAll('.filter-chip').forEach(c => {
        c.classList.remove('active', 'chip-charging', 'chip-maintain');
    });
    document.querySelectorAll('.filter-chip[data-value="all"]').forEach(c => c.classList.add('active'));
    document.getElementById('vitri-select').value = 'all';
    applyFilter(); renderCards(); renderTable();
}

function populateViTriSelect() {
    const sel = document.getElementById('vitri-select');
    if (!sel || sel.dataset.populated === 'true') return;
    const vitris = [...new Set(state.stations.map(s => s.viTri))].sort();
    vitris.forEach(v => {
        const opt = document.createElement('option');
        opt.value = v; opt.textContent = v;
        sel.appendChild(opt);
    });
    sel.dataset.populated = 'true';
}

// ============================================================
// MODAL – ADD STATION
// ============================================================
function openAddModal() {
    resetAddForm();
    openModal('add-modal');
}

function resetAddForm() {
    document.getElementById('add-id').value = '';
    document.getElementById('add-vitri').value = 'Da Lat';
    document.getElementById('add-congs').value = '';
    document.getElementById('add-preview').innerHTML = '<span style="color:var(--text-muted)">Nhap thong tin de xem truoc ten tram...</span>';
    clearFormErrors();
}

function clearFormErrors() {
    document.querySelectorAll('.form-error').forEach(e => e.classList.remove('visible'));
    document.querySelectorAll('.form-input').forEach(i => i.classList.remove('invalid'));
}

function updateAddPreview() {
    const viTri = document.getElementById('add-vitri').value;
    const cs = parseFloat(document.getElementById('add-congs').value);
    const preview = document.getElementById('add-preview');

    if (!viTri || isNaN(cs) || cs < 7) {
        preview.innerHTML = '<span style="color:var(--text-muted)">Nhap du thong tin de xem truoc...</span>';
        return;
    }

    const loai = classifyByPower(cs);
    const type = getStationType(loai);
    const stt = state.stations.filter(s => s.viTri === viTri).length + 1;
    const name = buildTenTram(loai, viTri, stt);

    preview.innerHTML = `
    <div class="preview-name">⚡ ${name}</div>
    <div class="preview-type">Phan loai: <span class="type-badge ${type.cls}">${type.label}</span> (${type.power})</div>
  `;
}

async function submitAddStation() {
    clearFormErrors();
    let valid = true;

    const id = document.getElementById('add-id').value.trim();
    const viTri = document.getElementById('add-vitri').value;
    const csRaw = document.getElementById('add-congs').value.trim();
    const cs = parseFloat(csRaw);

    // Validate ID
    if (!id) {
        showFieldError('add-id', 'add-id-err', 'ID khong duoc de trong.');
        valid = false;
    } else if (state.stations.some(s => s.maTram.toUpperCase() === id.toUpperCase())) {
        showFieldError('add-id', 'add-id-err', `ID "${id}" da ton tai trong he thong!`);
        valid = false;
    }

    // Validate congSuat
    if (!csRaw || isNaN(cs)) {
        showFieldError('add-congs', 'add-cs-err', 'Cong suat phai la mot so.');
        valid = false;
    } else if (cs < 7) {
        showFieldError('add-congs', 'add-cs-err', 'Cong suat toi thieu la 7 kW.');
        valid = false;
    } else if (cs > 300) {
        showFieldError('add-congs', 'add-cs-err', 'Cong suat toi da la 300 kW.');
        valid = false;
    }

    if (!valid) return;

    const loai = classifyByPower(cs);
    const stt = state.stations.filter(s => s.viTri === viTri).length + 1;
    const name = buildTenTram(loai, viTri, stt);

    const newStation = {
        maTram: id.toUpperCase(),
        loai, tenTram: name, viTri,
        congSuat: cs, trangThai: true,
        thoiGianHoatDong: 0,
    };

    try {
        const btn = document.getElementById('btn-submit-add');
        btn.disabled = true;
        btn.innerHTML = '<span class="loading-spinner"></span> Dang them...';

        await apiAddStation(newStation);
        closeModal('add-modal');
        applyFilter();
        renderAll();
        showToast(`Da them thanh cong: ${name}`, 'success');
    } catch (err) {
        showToast('Them tram that bai: ' + err.message, 'error');
    } finally {
        const btn = document.getElementById('btn-submit-add');
        if (btn) { btn.disabled = false; btn.innerHTML = '➕ Them tram'; }
    }
}

function showFieldError(inputId, errId, msg) {
    document.getElementById(inputId)?.classList.add('invalid');
    const err = document.getElementById(errId);
    if (err) { err.textContent = msg; err.classList.add('visible'); }
}

// ============================================================
// MODAL – UPDATE STATUS
// ============================================================
function openStatusModal(id) {
    const s = state.stations.find(x => x.maTram === id);
    if (!s) { showToast('Khong tim thay tram ' + id, 'error'); return; }

    state.editingId = id;
    setInner('status-modal-title', `Cap nhat: ${s.tenTram}`);
    setInner('status-modal-id', s.maTram);

    // Set current selection
    const curStatus = getStationStatus(s);
    document.querySelectorAll('.radio-option').forEach(opt => {
        const val = opt.dataset.value;
        opt.classList.remove('selected-ready', 'selected-charging', 'selected-maintain');
        if (val === curStatus) opt.classList.add(`selected-${curStatus}`);
    });

    // Set hours
    document.getElementById('status-hours').value = s.thoiGianHoatDong;

    openModal('status-modal');
}

function selectRadioOption(el) {
    const val = el.dataset.value;
    document.querySelectorAll('.radio-option').forEach(opt => {
        opt.classList.remove('selected-ready', 'selected-charging', 'selected-maintain');
    });
    el.classList.add(`selected-${val}`);
}

async function submitStatusUpdate() {
    const id = state.editingId;
    if (!id) return;

    const selectedOpt = document.querySelector('.radio-option.selected-ready, .radio-option.selected-charging, .radio-option.selected-maintain');
    const statusVal = selectedOpt ? selectedOpt.dataset.value : null;
    const hours = parseFloat(document.getElementById('status-hours').value);

    if (!statusVal) { showToast('Vui long chon trang thai!', 'warning'); return; }
    if (isNaN(hours) || hours < 0) { showToast('Thoi gian hoat dong phai >= 0!', 'warning'); return; }

    const updates = {
        trangThai: statusVal === 'ready',
        thoiGianHoatDong: hours,
    };

    try {
        await apiUpdateStatus(id, updates);
        closeModal('status-modal');
        applyFilter();
        renderAll();
        showToast(`Cap nhat tram ${id} thanh cong!`, 'success');
    } catch (err) {
        showToast('Cap nhat that bai: ' + err.message, 'error');
    }
}

// ============================================================
// DELETE
// ============================================================
function confirmDelete(id) {
    const s = state.stations.find(x => x.maTram === id);
    if (!s) return;
    state.editingId = id;
    setInner('delete-modal-name', s.tenTram);
    setInner('delete-modal-id', s.maTram);
    openModal('delete-modal');
}

async function submitDelete() {
    const id = state.editingId;
    if (!id) return;
    try {
        await apiDeleteStation(id);
        closeModal('delete-modal');
        applyFilter();
        renderAll();
        showToast(`Da xoa tram ${id}`, 'success');
    } catch (err) {
        showToast('Xoa that bai: ' + err.message, 'error');
    }
}

// ============================================================
// MODAL OPEN/CLOSE
// ============================================================
function openModal(id) {
    document.getElementById(id)?.classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closeModal(id) {
    document.getElementById(id)?.classList.remove('open');
    document.body.style.overflow = '';
}

// Close on overlay click
document.addEventListener('click', e => {
    if (e.target.classList.contains('modal-overlay')) {
        e.target.classList.remove('open');
        document.body.style.overflow = '';
    }
});

// Close on Escape key
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        document.querySelectorAll('.modal-overlay.open').forEach(m => {
            m.classList.remove('open');
            document.body.style.overflow = '';
        });
    }
});

// ============================================================
// TOAST NOTIFICATIONS
// ============================================================
function showToast(msg, type = 'success') {
    const container = document.getElementById('toast-container');
    const icons = { success: '✅', error: '❌', warning: '⚠️' };
    const div = document.createElement('div');
    div.className = `toast ${type}`;
    div.innerHTML = `<span class="toast-icon">${icons[type] || '💬'}</span><span class="toast-msg">${msg}</span>`;
    container.appendChild(div);
    setTimeout(() => {
        div.classList.add('removing');
        setTimeout(() => div.remove(), 300);
    }, 3500);
}

// ============================================================
// CLOCK
// ============================================================
function updateClock() {
    const el = document.getElementById('sidebar-clock');
    if (!el) return;
    const now = new Date();
    el.textContent = now.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
}

// ============================================================
// HELPERS DOM
// ============================================================
function setInner(id, val) {
    const el = document.getElementById(id);
    if (el) el.innerHTML = val;
}

// ============================================================
// TABLE SORT
// ============================================================
function sortTable(col) {
    if (state.sortCol === col) {
        state.sortDir = state.sortDir === 'asc' ? 'desc' : 'asc';
    } else {
        state.sortCol = col;
        state.sortDir = 'asc';
    }

    state.filtered.sort((a, b) => {
        let va = a[col], vb = b[col];
        if (typeof va === 'string') va = va.toLowerCase(), vb = vb.toLowerCase();
        if (va < vb) return state.sortDir === 'asc' ? -1 : 1;
        if (va > vb) return state.sortDir === 'asc' ? 1 : -1;
        return 0;
    });

    // Update sort icons
    document.querySelectorAll('.data-table th').forEach(th => {
        th.classList.remove('sorted-asc', 'sorted-desc');
        const s = th.querySelector('.sort-icon');
        if (s) s.textContent = '↕';
    });
    const activeTh = document.querySelector(`th[data-col="${col}"]`);
    if (activeTh) {
        const cls = 'sorted-' + state.sortDir;
        activeTh.classList.add(cls);
        const s = activeTh.querySelector('.sort-icon');
        if (s) s.textContent = state.sortDir === 'asc' ? '↑' : '↓';
    }

    state.tablePage = 1;
    renderTable();
}

// ============================================================
// AUTO REFRESH
// ============================================================
function startAutoRefresh() {
    if (state.refreshTimer) clearInterval(state.refreshTimer);
    state.refreshTimer = setInterval(() => {
        fetchStations();
    }, CONFIG.REFRESH_INTERVAL);
}

// ============================================================
// INIT
// ============================================================
document.addEventListener('DOMContentLoaded', () => {
    // Initial data fetch
    fetchStations();

    // Clock
    updateClock();
    setInterval(updateClock, 1000);

    // Auto refresh
    startAutoRefresh();

    // Default page
    navigate('dashboard');

    // Search input
    document.getElementById('search-input')?.addEventListener('input', onSearch);

    // Add form fields → update preview
    document.getElementById('add-vitri')?.addEventListener('change', updateAddPreview);
    document.getElementById('add-congs')?.addEventListener('input', updateAddPreview);

    // View toggle defaults
    document.getElementById('btn-view-card')?.classList.add('active');

    console.log('✅ Smart EV Dashboard initialized');
});
