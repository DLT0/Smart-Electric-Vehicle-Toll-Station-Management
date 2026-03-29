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

    // Export app state for debugging
    window.evApp = {
        state: appState,
        navigate: navigateToPage,
        search: performSearch
    };

    console.log("Smart EV Station Management System - Ready!");
});
