// Sidebar Management System
class SidebarManager {
    constructor() {
        this.sidebar = null;
        this.overlay = null;
        this.isOpen = true;
        this.isMobile = window.innerWidth < 1024;

        this.init();
        this.setupEventListeners();
    }

    init() {
        this.sidebar = document.getElementById('sidebar');
        this.overlay = document.getElementById('sidebar-overlay');
        this.mainContent = document.getElementById('main-content');

        // Thiết lập trạng thái ban đầu dựa trên kích thước màn hình
        if (this.isMobile) {
            this.isOpen = false;
            this.updateSidebarState();
        }
    }

    setupEventListeners() {
        // Toggle button
        const toggleBtn = document.getElementById('sidebar-toggle');
        if (toggleBtn) {
            toggleBtn.addEventListener('click', () => this.toggle());
        }

        // Overlay click để đóng sidebar (mobile)
        if (this.overlay) {
            this.overlay.addEventListener('click', () => {
                if (this.isMobile) {
                    this.close();
                }
            });
        }

        // Responsive behavior
        window.addEventListener('resize', () => {
            const wasMobile = this.isMobile;
            this.isMobile = window.innerWidth < 1024;

            // Nếu chuyển từ mobile sang desktop
            if (wasMobile && !this.isMobile) {
                this.isOpen = true;
                this.updateSidebarState();
            }
            // Nếu chuyển từ desktop sang mobile
            else if (!wasMobile && this.isMobile) {
                this.isOpen = false;
                this.updateSidebarState();
            }
        });

        // ESC key để đóng sidebar (mobile)
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && this.isOpen && this.isMobile) {
                this.close();
            }
        });
    }

    toggle() {
        if (this.isOpen) {
            this.close();
        } else {
            this.open();
        }
    }

    open() {
        this.isOpen = true;
        this.updateSidebarState();

        // Emit custom event
        window.dispatchEvent(new CustomEvent('sidebar:opened'));
    }

    close() {
        this.isOpen = false;
        this.updateSidebarState();

        // Emit custom event
        window.dispatchEvent(new CustomEvent('sidebar:closed'));
    }

    updateSidebarState() {
        if (!this.sidebar || !this.mainContent) return;

        if (this.isOpen) {
            // Sidebar mở
            this.sidebar.classList.remove('-translate-x-full');
            this.sidebar.classList.add('translate-x-0');

            if (this.isMobile) {
                // Mobile: hiển thị overlay
                this.overlay?.classList.remove('hidden');
                this.overlay?.classList.add('opacity-50');
                document.body.style.overflow = 'hidden'; // Prevent background scroll
            } else {
                // Desktop: điều chỉnh margin của main content
                this.mainContent.classList.remove('ml-0');
                this.mainContent.classList.add('ml-64');
                this.updateTopBarWidth(true);
            }
        } else {
            // Sidebar đóng
            this.sidebar.classList.remove('translate-x-0');
            this.sidebar.classList.add('-translate-x-full');

            if (this.isMobile) {
                // Mobile: ẩn overlay
                this.overlay?.classList.remove('opacity-50');
                this.overlay?.classList.add('hidden');
                document.body.style.overflow = ''; // Restore scroll
            } else {
                // Desktop: điều chỉnh margin của main content
                this.mainContent.classList.remove('ml-64');
                this.mainContent.classList.add('ml-0');
                this.updateTopBarWidth(false);
            }
        }

        // Update toggle button icon
        this.updateToggleIcon();
    }

    updateTopBarWidth(sidebarOpen) {
        const topBar = document.getElementById('top-bar');
        if (!topBar) return;

        if (this.isMobile) {
            // Mobile: luôn full width
            topBar.className = topBar.className.replace(/w-\[calc\(100%-16rem\)\]|w-full/g, 'w-full');
        } else {
            // Desktop: điều chỉnh theo sidebar
            if (sidebarOpen) {
                topBar.className = topBar.className.replace(/w-full/g, 'w-[calc(100%-16rem)]');
            } else {
                topBar.className = topBar.className.replace(/w-\[calc\(100%-16rem\)\]/g, 'w-full');
            }
        }
    }

    updateToggleIcon() {
        const toggleIcon = document.querySelector('#sidebar-toggle .material-symbols-outlined');
        if (toggleIcon) {
            if (this.isOpen) {
                toggleIcon.textContent = 'menu_open';
            } else {
                toggleIcon.textContent = 'menu';
            }
        }
    }

    // Public methods for external control
    getSidebarState() {
        return {
            isOpen: this.isOpen,
            isMobile: this.isMobile
        };
    }

    // Method để sync active menu item
    setActiveMenuItem(menuId) {
        // Remove active from all items
        document.querySelectorAll('.sidebar-menu-item').forEach(item => {
            item.classList.remove('text-[#75ff9e]', 'bg-[#171f33]', 'rounded-r-full', 'border-r-4', 'border-[#75ff9e]', 'translate-x-1');
            item.classList.add('text-slate-400', 'hover:text-slate-100');
        });

        // Add active to selected item
        const activeItem = document.getElementById(menuId);
        if (activeItem) {
            activeItem.classList.remove('text-slate-400', 'hover:text-slate-100');
            activeItem.classList.add('text-[#75ff9e]', 'bg-[#171f33]', 'rounded-r-full', 'border-r-4', 'border-[#75ff9e]', 'translate-x-1');
        }
    }
}

// Initialize khi DOM loaded
document.addEventListener('DOMContentLoaded', () => {
    window.sidebarManager = new SidebarManager();
});

// Export cho các file khác sử dụng
if (typeof module !== 'undefined' && module.exports) {
    module.exports = SidebarManager;
}
