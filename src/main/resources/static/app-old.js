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

        // Setup navigation handlers
        setupNavigation();

        // Setup search functionality
        setupSearch();

        // Setup notification handlers
        setupNotifications();

        // Setup Add Station Modal
        setupAddStationModal();

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
    };

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

    function setupAddStationModal() {
        const modal = document.getElementById('add-station-modal');
        const addStationBtn = document.getElementById('add-station-btn');
        const closeBtn = document.getElementById('close-add-station-modal');
        const cancelBtn = document.getElementById('cancel-add-station');
        const confirmBtn = document.getElementById('confirm-add-station');

        // Open modal
        if (addStationBtn) {
            addStationBtn.addEventListener('click', openAddStationModal);
        }

        // Close modal
        [closeBtn, cancelBtn].forEach(btn => {
            if (btn) {
                btn.addEventListener('click', closeAddStationModal);
            }
        });

        // Close on overlay click
        if (modal) {
            modal.addEventListener('click', (e) => {
                if (e.target === modal) {
                    closeAddStationModal();
                }
            });
        }

        // Confirm add station
        if (confirmBtn) {
            confirmBtn.addEventListener('click', handleAddStation);
        }

        // Setup region selection
        setupRegionSelection();

        // Setup power slider
        setupPowerSlider();

        // ESC key to close modal
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && modal && !modal.classList.contains('hidden')) {
                closeAddStationModal();
            }
        });
    }

    function openAddStationModal() {
        const modal = document.getElementById('add-station-modal');
        if (modal) {
            modal.classList.remove('hidden');
            document.body.style.overflow = 'hidden';
            console.log('Add Station modal opened');
        }
    }

    function closeAddStationModal() {
        const modal = document.getElementById('add-station-modal');
        if (modal) {
            modal.classList.add('hidden');
            document.body.style.overflow = '';
            console.log('Add Station modal closed');
        }
    }

    function setupRegionSelection() {
        const regionBtns = document.querySelectorAll('.region-btn');
        const stationIdPreview = document.getElementById('station-id-preview');

        regionBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                // Remove active state from all buttons
                regionBtns.forEach(b => {
                    b.classList.remove('bg-primary/20', 'border-primary', 'text-primary');
                    b.classList.add('bg-surface-container-low', 'border-transparent', 'text-on-surface-variant');
                    const firstSpan = b.querySelector('span:first-child');
                    if (firstSpan) {
                        firstSpan.classList.add('opacity-30');
                        firstSpan.classList.remove('opacity-70');
                    }
                });

                // Add active state to clicked button
                btn.classList.add('bg-primary/20', 'border-primary', 'text-primary');
                btn.classList.remove('bg-surface-container-low', 'border-transparent', 'text-on-surface-variant');
                const firstSpan = btn.querySelector('span:first-child');
                if (firstSpan) {
                    firstSpan.classList.remove('opacity-30');
                    firstSpan.classList.add('opacity-70');
                }

                // Update station ID preview
                const region = btn.dataset.region;
                const regionCode = getRegionCode(region);
                const randomNumber = Math.floor(Math.random() * 900) + 100; // 100-999
                if (stationIdPreview) {
                    stationIdPreview.textContent = `SN-${regionCode}-${randomNumber}`;
                }
            });
        });
    }

    function setupPowerSlider() {
        const powerSlider = document.getElementById('power-slider');
        const powerFill = document.getElementById('power-fill');
        const powerDisplay = document.getElementById('power-display');
        const powerInput = document.getElementById('power-input');
        const efficiencyDisplay = document.getElementById('efficiency-display');
        const stationClassPreview = document.getElementById('station-class-preview');

        let isDragging = false;

        if (powerSlider) {
            powerSlider.addEventListener('mousedown', startDrag);
            document.addEventListener('mousemove', drag);
            document.addEventListener('mouseup', endDrag);

            // Touch events for mobile
            powerSlider.addEventListener('touchstart', startDrag);
            document.addEventListener('touchmove', drag);
            document.addEventListener('touchend', endDrag);
        }

        // Power input field
        if (powerInput) {
            powerInput.addEventListener('input', (e) => {
                let value = parseInt(e.target.value) || 7;
                value = Math.max(7, Math.min(300, value)); // Clamp between 7-300
                updatePowerValues(value);
                updateSliderPosition(value);
            });
        }

        function startDrag(e) {
            isDragging = true;
            e.preventDefault();
        }

        function drag(e) {
            if (!isDragging) return;

            const container = powerSlider.parentElement;
            const rect = container.getBoundingClientRect();
            const x = (e.clientX || e.touches?.[0]?.clientX) - rect.left;
            const percentage = Math.max(0, Math.min(1, x / rect.width));

            const power = Math.round(7 + (293 * percentage)); // 7kW to 300kW
            updatePowerValues(power);
            updateSliderPosition(power);
        }

        function endDrag() {
            isDragging = false;
        }

        function updatePowerValues(power) {
            if (powerDisplay) powerDisplay.textContent = power;
            if (powerInput) powerInput.value = power;

            // Update efficiency (simulated calculation)
            const efficiency = Math.max(85, Math.min(99, 100 - (power - 7) * 0.05));
            if (efficiencyDisplay) efficiencyDisplay.textContent = `${efficiency.toFixed(1)}%`;

            // Update station classification
            let classification = '';
            if (power >= 7 && power < 22) {
                classification = 'Slow (7-20kW)';
            } else if (power >= 22 && power < 150) {
                classification = 'Fast (22-150kW)';
            } else {
                classification = 'Ultra Fast (150kW+)';
            }

            if (stationClassPreview) stationClassPreview.textContent = classification;
        }

        function updateSliderPosition(power) {
            const percentage = (power - 7) / 293; // 0-1 range
            if (powerSlider) powerSlider.style.left = `${percentage * 100}%`;
            if (powerFill) powerFill.style.width = `${percentage * 100}%`;
        }
    }

    function getRegionCode(region) {
        const codes = {
            'da-lat': 'DAL',
            'bao-loc': 'BAL',
            'duc-trong': 'DTR',
            'di-linh': 'DIL',
            'lam-ha': 'LAH',
            'don-duong': 'DDU'
        };
        return codes[region] || 'DAL';
    }

    function handleAddStation() {
        // Get form values
        const selectedRegion = document.querySelector('.region-btn.bg-primary\\/20');
        const power = document.getElementById('power-input')?.value;
        const stationId = document.getElementById('station-id-preview')?.textContent;

        if (!selectedRegion) {
            alert('Please select a region');
            return;
        }

        // Simulate adding station
        console.log('Adding new station:', {
            region: selectedRegion.dataset.region,
            power: parseInt(power),
            stationId: stationId
        });

        // Show success message
        showSuccessNotification(`Station ${stationId} added successfully!`);

        // Close modal
        closeAddStationModal();

        // Reset form (optional)
        resetAddStationForm();
    }

    function resetAddStationForm() {
        // Reset to Da Lat as default
        const firstRegionBtn = document.querySelector('.region-btn[data-region="da-lat"]');
        if (firstRegionBtn) firstRegionBtn.click();

        // Reset power to 120kW
        const powerInput = document.getElementById('power-input');
        if (powerInput) {
            powerInput.value = 120;
            powerInput.dispatchEvent(new Event('input'));
        }
    }

    function showSuccessNotification(message) {
        const notification = document.createElement('div');
        notification.className = 'fixed top-24 right-8 bg-primary text-on-primary px-6 py-3 rounded-lg shadow-lg z-50 transform translate-x-full transition-transform duration-300';
        notification.innerHTML = `
            <div class="flex items-center gap-2">
                <span class="material-symbols-outlined text-sm">check_circle</span>
                <span class="font-medium">${message}</span>
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
    search: performSearch
};

console.log("Smart EV Station Management System - Ready!");
});
