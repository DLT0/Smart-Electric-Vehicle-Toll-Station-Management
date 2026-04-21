// Smart EV Station Management System - Main Application
console.log("Smart EV Station Management System - Loading...");

// Application initialization
document.addEventListener('DOMContentLoaded', () => {
    console.log("DOM loaded, initializing application...");

    // Application state
    const appState = {
        currentPage: 'dashboard',
        user: null,
        settings: {
            theme: 'dark',
            notifications: true
        }
    };

    // Initialize application components
    initializeApp();

    function initializeApp() {
        console.log("Initializing EV Management Console...");

        // Setup navigation handlers
        setupNavigation();

        // Setup search functionality
        setupSearch();

        // Setup notification handlers
        setupNotifications();

        // Setup modal handlers
        setupModalHandlers();

        // Listen for sidebar events
        listenToSidebarEvents();

        // Fetch Dashboard Data from Java Backend
        loadDashboardData();

        console.log("Application initialized successfully!");
    }

    async function loadDashboardData() {
        try {
            console.log("Fetching dashboard data from backend...");
            const response = await fetch('/api/dashboard/stats');

            if (response.ok) {
                const data = await response.json();

                // Update DOM with data from Java
                const totalStationsEl = document.getElementById('total-stations');
                if (totalStationsEl) totalStationsEl.textContent = data.totalStations;

                const activeSessionsEl = document.getElementById('active-sessions');
                if (activeSessionsEl) activeSessionsEl.textContent = data.activeSessions;

                const revenueEl = document.getElementById('revenue');
                if (revenueEl) revenueEl.innerHTML = `${data.revenue}<span class="text-xl ml-1 text-slate-400"></span>`;

                console.log("Dashboard data updated successfully!");
            } else {
                console.warn("Failed to fetch dashboard data. Status:", response.status);
            }
        } catch (error) {
            console.error("Error connecting to Java backend. Make sure the server is running.", error);
        }
    }

    function setupNavigation() {
        // Menu item click handlers
        const menuItems = document.querySelectorAll('.sidebar-menu-item');

        menuItems.forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const menuId = item.id.replace('menu-', '');
                navigateToPage(menuId);

                // Update active menu item using sidebar manager
                if (window.sidebarManager) {
                    window.sidebarManager.setActiveMenuItem(item.id);
                }

                // Close sidebar on mobile after selection
                if (window.sidebarManager && window.sidebarManager.getSidebarState().isMobile) {
                    setTimeout(() => {
                        window.sidebarManager.close();
                    }, 200);
                }
            });
        });
    }

    function setupSearch() {
        const searchInput = document.querySelector('input[placeholder*="Search"]');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                const query = e.target.value.trim();
                if (query.length > 2) {
                    performSearch(query);
                }
            });
        }
    }

    function setupNotifications() {
        const notificationBtn = document.querySelector('button[class*="notifications"]').parentElement;
        if (notificationBtn) {
            notificationBtn.addEventListener('click', () => {
                showNotifications();
            });
        }
    }

    function listenToSidebarEvents() {
        // Listen to sidebar state changes
        window.addEventListener('sidebar:opened', () => {
            console.log('Sidebar opened');
            // Update analytics or perform other actions
        });

        window.addEventListener('sidebar:closed', () => {
            console.log('Sidebar closed');
        });
    }

    function navigateToPage(pageId) {
        console.log(`Navigating to: ${pageId}`);
        appState.currentPage = pageId;

        // Here you would normally load different content based on pageId
        // For now, we'll just update the document title and show a message

        const pageTitle = getPageTitle(pageId);
        document.title = `${pageTitle} - VN EV Management Console`;

        // Show page transition indicator (optional)
        showPageTransition(pageTitle);
    }

    function getPageTitle(pageId) {
        const titles = {
            'dashboard': 'Dashboard',
            'stations': 'Station List',
            'statistics': 'Statistics',
            'maintenance': 'Maintenance',
            'settings': 'Settings'
        };
        return titles[pageId] || 'Dashboard';
    }

    function showPageTransition(pageTitle) {
        // Simple notification to user about page change
        // In a real app, this would be more sophisticated
        const existingNotification = document.querySelector('.page-transition-notification');
        if (existingNotification) {
            existingNotification.remove();
        }

        const notification = document.createElement('div');
        notification.className = 'page-transition-notification fixed top-24 right-8 bg-primary text-on-primary px-4 py-2 rounded-lg shadow-lg z-50 transform translate-x-full transition-transform duration-300';
        notification.textContent = `Switched to ${pageTitle}`;

        document.body.appendChild(notification);

        // Animation
        setTimeout(() => {
            notification.classList.remove('translate-x-full');
        }, 100);

        setTimeout(() => {
            notification.classList.add('translate-x-full');
            setTimeout(() => {
                notification.remove();
            }, 300);
        }, 2000);
    }

    function performSearch(query) {
        console.log(`Searching for: ${query}`);
        // Implement search logic here
        // This could search through stations, maintenance records, etc.
    }

    function showNotifications() {
        console.log('Showing notifications...');
        // Implement notification panel here
        // Could show alerts, maintenance due dates, etc.
    }

    async function setupModalHandlers() {
        console.log('Loading modal from external file: add-station-modal.html');

        try {
            // Fetch modal từ file HTML riêng
            const response = await fetch('add-station-modal.html');
            if (!response.ok) throw new Error("Could not load add-station-modal.html");
            const text = await response.text();

            // Phân tích HTML
            const parser = new DOMParser();
            const doc = parser.parseFromString(text, 'text/html');
            // Lấy thẻ chứa modal (thẻ div có class fixed inset-0)
            const modalHTML = doc.querySelector('.fixed.inset-0.z-50');

            if (modalHTML) {
                // Xóa modal cũ nếu có trong index.html
                const oldModal = document.getElementById('add-station-modal');
                if (oldModal) oldModal.remove();

                // Bổ sung các ID cần thiết vào modal lấy từ file để JS có thể nhận diện
                modalHTML.id = 'add-station-modal';
                modalHTML.classList.add('hidden'); // Đảm bảo modal ẩn lúc đầu

                // Gắn ID cho nút Đóng
                const closeBtnDoc = modalHTML.querySelector('button[class*="absolute top-4"]');
                if (closeBtnDoc) closeBtnDoc.id = 'close-add-station-modal';

                // Gắn ID cho nút Xác nhận và Hủy
                const buttons = Array.from(modalHTML.querySelectorAll('button'));
                const confirmBtnDoc = buttons.find(b => b.textContent.includes('Add Station'));
                if (confirmBtnDoc) confirmBtnDoc.id = 'confirm-add-station';

                const cancelBtnDoc = buttons.find(b => b.textContent.includes('Cancel'));
                if (cancelBtnDoc) cancelBtnDoc.id = 'cancel-add-station';

                // Gắn modal vào thẻ body của index.html
                document.body.appendChild(modalHTML);
                console.log("Modal đã được nhúng thành công vào trang!");
            }
        } catch (error) {
            console.error("Lỗi khi nhúng modal:", error);
            return;
        }

        // --- SAU KHI ĐÃ NHÚNG THÀNH CÔNG, BẮT ĐẦU GẮN SỰ KIỆN TƯƠNG TÁC ---
        const addStationBtn = document.getElementById('add-station-btn');
        const addStationModal = document.getElementById('add-station-modal');
        const closeModalBtn = document.getElementById('close-add-station-modal');
        const cancelBtn = document.getElementById('cancel-add-station');
        const confirmBtn = document.getElementById('confirm-add-station');

        // Các vùng chọn và slider
        const regionButtons = document.querySelectorAll('#add-station-modal .grid.grid-cols-3 button');
        const powerSlider = document.getElementById('power-level');
        const powerInput = document.querySelector('#add-station-modal input[type="number"]');

        let selectedRegion = 'da-lat';
        let selectedPower = 120;

        // Open modal
        if (addStationBtn) {
            addStationBtn.addEventListener('click', () => {
                console.log('Opening add station modal...');
                addStationModal.classList.remove('hidden');
                addStationModal.classList.add('flex');
                document.body.classList.add('overflow-hidden');

                // Reset form
                resetModalForm();
            });
        }

        // Close modal functions
        function closeModal() {
            console.log('Closing add station modal...');
            addStationModal.classList.add('hidden');
            addStationModal.classList.remove('flex');
            document.body.classList.remove('overflow-hidden');
        }

        // Close modal event listeners
        if (closeModalBtn) {
            closeModalBtn.addEventListener('click', closeModal);
        }

        if (cancelBtn) {
            cancelBtn.addEventListener('click', closeModal);
        }

        // Close on backdrop click
        if (addStationModal) {
            addStationModal.addEventListener('click', (e) => {
                if (e.target === addStationModal) {
                    closeModal();
                }
            });
        }

        // Region selection
        regionButtons.forEach(button => {
            button.addEventListener('click', () => {
                // Remove active state from all buttons
                regionButtons.forEach(btn => btn.classList.remove('ring-2', 'ring-primary'));

                // Add active state to clicked button
                button.classList.add('ring-2', 'ring-primary');

                // Update selected region
                selectedRegion = button.dataset.region;
                if (previewRegion) {
                    previewRegion.textContent = button.textContent.trim();
                }

                console.log(`Region selected: ${selectedRegion}`);
            });
        });

        // Power slider
        if (powerSlider) {
            powerSlider.addEventListener('input', (e) => {
                selectedPower = parseInt(e.target.value);
                if (powerValue) {
                    powerValue.textContent = `${selectedPower}kW`;
                }
                if (previewPower) {
                    previewPower.textContent = `${selectedPower}kW`;
                }

                console.log(`Power level selected: ${selectedPower}kW`);
            });
        }

        // Confirm add station
        if (confirmBtn) {
            confirmBtn.addEventListener('click', async () => {
                console.log('Confirming add station...');

                // Get form data
                const stationName = document.getElementById('station-name')?.value;
                const stationAddress = document.getElementById('station-address')?.value;

                // Basic validation
                if (!stationName || !stationAddress) {
                    showErrorNotification('Vui lòng điền đầy đủ thông tin trạm sạc');
                    return;
                }

                // Create station data
                const stationData = {
                    name: stationName,
                    address: stationAddress,
                    region: selectedRegion,
                    powerLevel: selectedPower,
                    status: 'active',
                    createdAt: new Date().toISOString()
                };

                console.log('Station data:', stationData);

                try {
                    // Here you would normally send to backend
                    // const response = await fetch('/api/stations', {
                    //     method: 'POST',
                    //     headers: { 'Content-Type': 'application/json' },
                    //     body: JSON.stringify(stationData)
                    // });

                    // Simulate success for now
                    await new Promise(resolve => setTimeout(resolve, 500));

                    showSuccessNotification(`Đã thêm trạm sạc "${stationName}" thành công!`);
                    closeModal();

                    // Refresh dashboard data
                    loadDashboardData();

                } catch (error) {
                    console.error('Error adding station:', error);
                    showErrorNotification('Có lỗi xảy ra khi thêm trạm sạc');
                }
            });
        }

        function resetModalForm() {
            // Reset form fields
            const stationNameInput = document.getElementById('station-name');
            const stationAddressInput = document.getElementById('station-address');

            if (stationNameInput) stationNameInput.value = '';
            if (stationAddressInput) stationAddressInput.value = '';

            // Reset region selection (default to first region)
            regionButtons.forEach(btn => btn.classList.remove('ring-2', 'ring-primary'));
            if (regionButtons.length > 0) {
                regionButtons[0].classList.add('ring-2', 'ring-primary');
                selectedRegion = regionButtons[0].dataset.region;
                if (previewRegion) {
                    previewRegion.textContent = regionButtons[0].textContent.trim();
                }
            }

            // Reset power slider
            if (powerSlider) {
                powerSlider.value = '50';
                selectedPower = 50;
                if (powerValue) powerValue.textContent = '50kW';
                if (previewPower) previewPower.textContent = '50kW';
            }
        }
    }

    function showSuccessNotification(message) {
        const notification = document.createElement('div');
        notification.className = 'fixed top-24 right-8 bg-green-600 text-white px-6 py-3 rounded-lg shadow-lg z-50 transform translate-x-full transition-transform duration-300';
        notification.innerHTML = `
            <div class="flex items-center gap-3">
                <span class="material-symbols-outlined">check_circle</span>
                <span>${message}</span>
            </div>
        `;

        document.body.appendChild(notification);

        // Animation
        setTimeout(() => {
            notification.classList.remove('translate-x-full');
        }, 100);

        setTimeout(() => {
            notification.classList.add('translate-x-full');
            setTimeout(() => {
                notification.remove();
            }, 300);
        }, 3000);
    }

    function showErrorNotification(message) {
        const notification = document.createElement('div');
        notification.className = 'fixed top-24 right-8 bg-red-600 text-white px-6 py-3 rounded-lg shadow-lg z-50 transform translate-x-full transition-transform duration-300';
        notification.innerHTML = `
            <div class="flex items-center gap-3">
                <span class="material-symbols-outlined">error</span>
                <span>${message}</span>
            </div>
        `;

        document.body.appendChild(notification);

        // Animation
        setTimeout(() => {
            notification.classList.remove('translate-x-full');
        }, 100);

        setTimeout(() => {
            notification.classList.add('translate-x-full');
            setTimeout(() => {
                notification.remove();
            }, 300);
        }, 3000);
    }

    // Export app state for debugging
    window.evApp = {
        state: appState,
        navigate: navigateToPage,
        search: performSearch
    };

    console.log("Smart EV Station Management System - Ready!");
});
