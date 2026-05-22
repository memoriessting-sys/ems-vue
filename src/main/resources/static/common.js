/**
 * 企业员工管理系统 - 现代化主题JavaScript
 * 提供通用的交互效果、动画和工具函数
 */

// 全局配置
const CONFIG = {
    // API基础URL
    API_BASE: '',

    // 动画持续时间
    ANIMATION_DURATION: 300,

    // 消息显示时间
    MESSAGE_DURATION: 5000,

    // 分页配置
    PAGE_SIZE: 10,

    // 主题配置
    THEME: {
        primary: '#667eea',
        secondary: '#764ba2',
        success: '#4facfe',
        warning: '#43e97b',
        danger: '#fa709a'
    }
};

// 工具函数
const Utils = {
    /**
     * 防抖函数
     * @param {Function} func 要防抖的函数
     * @param {number} wait 等待时间
     * @returns {Function} 防抖后的函数
     */
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },

    /**
     * 节流函数
     * @param {Function} func 要节流的函数
     * @param {number} limit 限制时间
     * @returns {Function} 节流后的函数
     */
    throttle(func, limit) {
        let inThrottle;
        return function () {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    },

    /**
     * 格式化日期
     * @param {Date|string} date 日期
     * @param {string} format 格式
     * @returns {string} 格式化后的日期
     */
    formatDate(date, format = 'YYYY-MM-DD') {
        const d = new Date(date);
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hours = String(d.getHours()).padStart(2, '0');
        const minutes = String(d.getMinutes()).padStart(2, '0');
        const seconds = String(d.getSeconds()).padStart(2, '0');

        return format
            .replace('YYYY', year)
            .replace('MM', month)
            .replace('DD', day)
            .replace('HH', hours)
            .replace('mm', minutes)
            .replace('ss', seconds);
    },

    /**
     * 格式化数字
     * @param {number} num 数字
     * @param {number} decimals 小数位数
     * @returns {string} 格式化后的数字
     */
    formatNumber(num, decimals = 0) {
        return Number(num).toLocaleString('zh-CN', {
            minimumFractionDigits: decimals,
            maximumFractionDigits: decimals
        });
    },

    /**
     * 生成随机ID
     * @param {number} length ID长度
     * @returns {string} 随机ID
     */
    generateId(length = 8) {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';
        for (let i = 0; i < length; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return result;
    },

    /**
     * 深拷贝对象
     * @param {*} obj 要拷贝的对象
     * @returns {*} 拷贝后的对象
     */
    deepClone(obj) {
        if (obj === null || typeof obj !== 'object') return obj;
        if (obj instanceof Date) return new Date(obj.getTime());
        if (obj instanceof Array) return obj.map(item => this.deepClone(item));
        if (typeof obj === 'object') {
            const clonedObj = {};
            for (const key in obj) {
                if (obj.hasOwnProperty(key)) {
                    clonedObj[key] = this.deepClone(obj[key]);
                }
            }
            return clonedObj;
        }
    },

    /**
     * 验证邮箱格式
     * @param {string} email 邮箱地址
     * @returns {boolean} 是否有效
     */
    isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },

    /**
     * 验证手机号格式
     * @param {string} phone 手机号
     * @returns {boolean} 是否有效
     */
    isValidPhone(phone) {
        const phoneRegex = /^1[3-9]\d{9}$/;
        return phoneRegex.test(phone);
    },

    /**
     * 获取URL参数
     * @param {string} name 参数名
     * @returns {string|null} 参数值
     */
    getUrlParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    },

    /**
     * 设置URL参数
     * @param {string} name 参数名
     * @param {string} value 参数值
     */
    setUrlParam(name, value) {
        const url = new URL(window.location);
        url.searchParams.set(name, value);
        window.history.replaceState({}, '', url);
    }
};

// 消息通知系统
const Notification = {
    /**
     * 显示成功消息
     * @param {string} message 消息内容
     * @param {number} duration 显示时间
     */
    success(message, duration = CONFIG.MESSAGE_DURATION) {
        this.show(message, 'success', duration);
    },

    /**
     * 显示错误消息
     * @param {string} message 消息内容
     * @param {number} duration 显示时间
     */
    error(message, duration = CONFIG.MESSAGE_DURATION) {
        this.show(message, 'danger', duration);
    },

    /**
     * 显示警告消息
     * @param {string} message 消息内容
     * @param {number} duration 显示时间
     */
    warning(message, duration = CONFIG.MESSAGE_DURATION) {
        this.show(message, 'warning', duration);
    },

    /**
     * 显示信息消息
     * @param {string} message 消息内容
     * @param {number} duration 显示时间
     */
    info(message, duration = CONFIG.MESSAGE_DURATION) {
        this.show(message, 'info', duration);
    },

    /**
     * 显示消息
     * @param {string} message 消息内容
     * @param {string} type 消息类型
     * @param {number} duration 显示时间
     */
    show(message, type = 'info', duration = CONFIG.MESSAGE_DURATION) {
        const id = Utils.generateId();
        const iconMap = {
            success: 'fas fa-check-circle',
            danger: 'fas fa-exclamation-triangle',
            warning: 'fas fa-exclamation-circle',
            info: 'fas fa-info-circle'
        };

        const notification = `
            <div id="notification-${id}" class="notification notification-${type} fade-in">
                <div class="notification-content">
                    <i class="${iconMap[type]}"></i>
                    <span>${message}</span>
                    <button class="notification-close" onclick="Notification.close('${id}')">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            </div>
        `;

        // 创建通知容器
        let container = document.getElementById('notification-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'notification-container';
            container.className = 'notification-container';
            document.body.appendChild(container);
        }

        container.insertAdjacentHTML('beforeend', notification);

        // 自动关闭
        if (duration > 0) {
            setTimeout(() => {
                this.close(id);
            }, duration);
        }

        return id;
    },

    /**
     * 关闭消息
     * @param {string} id 消息ID
     */
    close(id) {
        const notification = document.getElementById(`notification-${id}`);
        if (notification) {
            notification.classList.add('fade-out');
            setTimeout(() => {
                notification.remove();
            }, 300);
        }
    },

    /**
     * 关闭所有消息
     */
    closeAll() {
        const notifications = document.querySelectorAll('.notification');
        notifications.forEach(notification => {
            notification.classList.add('fade-out');
            setTimeout(() => {
                notification.remove();
            }, 300);
        });
    }
};

// 加载动画系统
const Loading = {
    /**
     * 显示加载动画
     * @param {string} message 加载消息
     * @param {string} target 目标元素
     */
    show(message = '加载中...', target = 'body') {
        const loadingId = Utils.generateId();
        const loadingHtml = `
            <div id="loading-${loadingId}" class="loading-overlay">
                <div class="loading-content">
                    <div class="spinner"></div>
                    <p class="loading-message">${message}</p>
                </div>
            </div>
        `;

        const targetElement = target === 'body' ? document.body : document.querySelector(target);
        targetElement.insertAdjacentHTML('beforeend', loadingHtml);

        return loadingId;
    },

    /**
     * 隐藏加载动画
     * @param {string} id 加载动画ID
     */
    hide(id) {
        const loading = document.getElementById(`loading-${id}`);
        if (loading) {
            loading.classList.add('fade-out');
            setTimeout(() => {
                loading.remove();
            }, 300);
        }
    },

    /**
     * 隐藏所有加载动画
     */
    hideAll() {
        const loadings = document.querySelectorAll('.loading-overlay');
        loadings.forEach(loading => {
            loading.classList.add('fade-out');
            setTimeout(() => {
                loading.remove();
            }, 300);
        });
    }
};

// 动画系统
const Animation = {
    /**
     * 淡入动画
     * @param {Element} element 目标元素
     * @param {number} duration 动画时长
     */
    fadeIn(element, duration = CONFIG.ANIMATION_DURATION) {
        element.style.opacity = '0';
        element.style.display = 'block';

        let start = null;
        const animate = (timestamp) => {
            if (!start) start = timestamp;
            const progress = timestamp - start;
            const opacity = Math.min(progress / duration, 1);

            element.style.opacity = opacity;

            if (progress < duration) {
                requestAnimationFrame(animate);
            }
        };

        requestAnimationFrame(animate);
    },

    /**
     * 淡出动画
     * @param {Element} element 目标元素
     * @param {number} duration 动画时长
     */
    fadeOut(element, duration = CONFIG.ANIMATION_DURATION) {
        let start = null;
        const animate = (timestamp) => {
            if (!start) start = timestamp;
            const progress = timestamp - start;
            const opacity = Math.max(1 - progress / duration, 0);

            element.style.opacity = opacity;

            if (progress < duration) {
                requestAnimationFrame(animate);
            } else {
                element.style.display = 'none';
            }
        };

        requestAnimationFrame(animate);
    },

    /**
     * 滑入动画
     * @param {Element} element 目标元素
     * @param {string} direction 方向
     * @param {number} duration 动画时长
     */
    slideIn(element, direction = 'up', duration = CONFIG.ANIMATION_DURATION) {
        const directions = {
            up: { transform: 'translateY(50px)', opacity: 0 },
            down: { transform: 'translateY(-50px)', opacity: 0 },
            left: { transform: 'translateX(50px)', opacity: 0 },
            right: { transform: 'translateX(-50px)', opacity: 0 }
        };

        const initial = directions[direction];
        element.style.transform = initial.transform;
        element.style.opacity = initial.opacity;
        element.style.display = 'block';

        let start = null;
        const animate = (timestamp) => {
            if (!start) start = timestamp;
            const progress = timestamp - start;
            const ratio = Math.min(progress / duration, 1);

            element.style.transform = `translate(${0}, ${0})`;
            element.style.opacity = ratio;

            if (progress < duration) {
                requestAnimationFrame(animate);
            }
        };

        requestAnimationFrame(animate);
    },

    /**
     * 数字动画
     * @param {Element} element 目标元素
     * @param {number} target 目标值
     * @param {number} duration 动画时长
     */
    animateNumber(element, target, duration = 1000) {
        const start = parseInt(element.textContent) || 0;
        const increment = (target - start) / (duration / 16);
        let current = start;

        const animate = () => {
            current += increment;
            if ((increment > 0 && current >= target) || (increment < 0 && current <= target)) {
                element.textContent = target;
            } else {
                element.textContent = Math.floor(current);
                requestAnimationFrame(animate);
            }
        };

        requestAnimationFrame(animate);
    }
};

// 表单验证系统
const Validation = {
    /**
     * 验证规则
     */
    rules: {
        required: (value) => value && value.toString().trim() !== '',
        email: (value) => Utils.isValidEmail(value),
        phone: (value) => Utils.isValidPhone(value),
        minLength: (value, min) => value && value.toString().length >= min,
        maxLength: (value, max) => value && value.toString().length <= max,
        numeric: (value) => !isNaN(value) && !isNaN(parseFloat(value)),
        url: (value) => {
            try {
                new URL(value);
                return true;
            } catch {
                return false;
            }
        }
    },

    /**
     * 验证表单
     * @param {HTMLFormElement} form 表单元素
     * @param {Object} rules 验证规则
     * @returns {Object} 验证结果
     */
    validateForm(form, rules) {
        const errors = {};
        const formData = new FormData(form);

        for (const [field, fieldRules] of Object.entries(rules)) {
            const value = formData.get(field);
            const fieldErrors = [];

            for (const [rule, params] of Object.entries(fieldRules)) {
                if (this.rules[rule]) {
                    const isValid = this.rules[rule](value, params);
                    if (!isValid) {
                        fieldErrors.push(this.getErrorMessage(rule, params));
                    }
                }
            }

            if (fieldErrors.length > 0) {
                errors[field] = fieldErrors;
            }
        }

        return {
            isValid: Object.keys(errors).length === 0,
            errors
        };
    },

    /**
     * 获取错误消息
     * @param {string} rule 规则名
     * @param {*} params 参数
     * @returns {string} 错误消息
     */
    getErrorMessage(rule, params) {
        const messages = {
            required: '此字段为必填项',
            email: '请输入有效的邮箱地址',
            phone: '请输入有效的手机号码',
            minLength: `最少需要 ${params} 个字符`,
            maxLength: `最多允许 ${params} 个字符`,
            numeric: '请输入有效的数字',
            url: '请输入有效的URL地址'
        };

        return messages[rule] || '验证失败';
    },

    /**
     * 显示表单错误
     * @param {HTMLFormElement} form 表单元素
     * @param {Object} errors 错误信息
     */
    showFormErrors(form, errors) {
        // 清除之前的错误
        form.querySelectorAll('.is-invalid').forEach(element => {
            element.classList.remove('is-invalid');
        });

        // 显示新的错误
        for (const [field, fieldErrors] of Object.entries(errors)) {
            const element = form.querySelector(`[name="${field}"]`);
            if (element) {
                element.classList.add('is-invalid');

                // 创建错误消息元素
                let errorElement = element.parentNode.querySelector('.invalid-feedback');
                if (!errorElement) {
                    errorElement = document.createElement('div');
                    errorElement.className = 'invalid-feedback';
                    element.parentNode.appendChild(errorElement);
                }

                errorElement.textContent = fieldErrors.join(', ');
            }
        }
    }
};

// 表格系统
const Table = {
    /**
     * 初始化表格
     * @param {string} tableId 表格ID
     * @param {Object} options 配置选项
     */
    init(tableId, options = {}) {
        const table = document.getElementById(tableId);
        if (!table) return;

        const defaultOptions = {
            pageSize: CONFIG.PAGE_SIZE,
            currentPage: 1,
            totalPages: 1,
            onPageChange: null,
            onSort: null,
            onSearch: null
        };

        this.options = { ...defaultOptions, ...options };
        this.table = table;
        this.bindEvents();
    },

    /**
     * 绑定事件
     */
    bindEvents() {
        // 分页事件
        this.table.addEventListener('click', (e) => {
            if (e.target.classList.contains('page-link')) {
                e.preventDefault();
                const page = parseInt(e.target.dataset.page);
                if (page && this.options.onPageChange) {
                    this.options.onPageChange(page);
                }
            }
        });

        // 排序事件
        this.table.addEventListener('click', (e) => {
            if (e.target.classList.contains('sortable')) {
                const column = e.target.dataset.column;
                const direction = e.target.dataset.direction === 'asc' ? 'desc' : 'asc';

                e.target.dataset.direction = direction;
                e.target.classList.toggle('sort-asc', direction === 'asc');
                e.target.classList.toggle('sort-desc', direction === 'desc');

                if (this.options.onSort) {
                    this.options.onSort(column, direction);
                }
            }
        });
    },

    /**
     * 更新表格数据
     * @param {Array} data 数据
     * @param {number} total 总数
     */
    updateData(data, total) {
        const tbody = this.table.querySelector('tbody');
        if (!tbody) return;

        tbody.innerHTML = '';

        if (data.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="100%" class="text-center text-muted">
                        <div class="empty-state">
                            <i class="fas fa-inbox fa-3x mb-3"></i>
                            <p>暂无数据</p>
                        </div>
                    </td>
                </tr>
            `;
            return;
        }

        data.forEach(row => {
            const tr = document.createElement('tr');
            tr.innerHTML = this.renderRow(row);
            tbody.appendChild(tr);
        });

        this.updatePagination(total);
    },

    /**
     * 渲染行数据
     * @param {Object} row 行数据
     * @returns {string} HTML字符串
     */
    renderRow(row) {
        // 这个方法应该根据具体的数据结构来实现
        return Object.values(row).map(value => `<td>${value || '-'}</td>`).join('');
    },

    /**
     * 更新分页
     * @param {number} total 总数
     */
    updatePagination(total) {
        const pagination = this.table.parentNode.querySelector('.pagination');
        if (!pagination) return;

        const totalPages = Math.ceil(total / this.options.pageSize);
        this.options.totalPages = totalPages;

        let paginationHtml = '';

        // 上一页
        if (this.options.currentPage > 1) {
            paginationHtml += `
                <a href="#" class="page-link" data-page="${this.options.currentPage - 1}">
                    <i class="fas fa-chevron-left"></i>
                </a>
            `;
        }

        // 页码
        const startPage = Math.max(1, this.options.currentPage - 2);
        const endPage = Math.min(totalPages, this.options.currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            paginationHtml += `
                <a href="#" class="page-link ${i === this.options.currentPage ? 'active' : ''}" data-page="${i}">
                    ${i}
                </a>
            `;
        }

        // 下一页
        if (this.options.currentPage < totalPages) {
            paginationHtml += `
                <a href="#" class="page-link" data-page="${this.options.currentPage + 1}">
                    <i class="fas fa-chevron-right"></i>
                </a>
            `;
        }

        pagination.innerHTML = paginationHtml;
    }
};

// 模态框系统
const Modal = {
    /**
     * 显示模态框
     * @param {string} title 标题
     * @param {string} content 内容
     * @param {Object} options 选项
     */
    show(title, content, options = {}) {
        const modalId = Utils.generateId();
        const defaultOptions = {
            size: 'modal-lg',
            backdrop: true,
            keyboard: true,
            onConfirm: null,
            onCancel: null,
            confirmText: '确定',
            cancelText: '取消',
            showCancel: true
        };

        const modalOptions = { ...defaultOptions, ...options };

        const modalHtml = `
            <div class="modal fade" id="modal-${modalId}" tabindex="-1" role="dialog">
                <div class="modal-dialog ${modalOptions.size}" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">${title}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            ${content}
                        </div>
                        <div class="modal-footer">
                            ${modalOptions.showCancel ?
                `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">${modalOptions.cancelText}</button>` :
                ''
            }
                            <button type="button" class="btn btn-primary" id="modal-confirm-${modalId}">${modalOptions.confirmText}</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', modalHtml);

        const modal = document.getElementById(`modal-${modalId}`);
        const confirmBtn = document.getElementById(`modal-confirm-${modalId}`);

        // 绑定确认事件
        if (modalOptions.onConfirm) {
            confirmBtn.addEventListener('click', () => {
                modalOptions.onConfirm();
                this.close(modalId);
            });
        }

        // 绑定取消事件
        if (modalOptions.onCancel) {
            modal.addEventListener('hidden.bs.modal', () => {
                modalOptions.onCancel();
            });
        }

        // 显示模态框
        const bsModal = new bootstrap.Modal(modal);
        bsModal.show();

        return modalId;
    },

    /**
     * 关闭模态框
     * @param {string} modalId 模态框ID
     */
    close(modalId) {
        const modal = document.getElementById(`modal-${modalId}`);
        if (modal) {
            $(modal).modal('hide');
            setTimeout(() => {
                modal.remove();
            }, 300);
        }
    },

    /**
     * 确认对话框
     * @param {string} message 消息
     * @param {Function} onConfirm 确认回调
     * @param {Function} onCancel 取消回调
     */
    confirm(message, onConfirm, onCancel) {
        return this.show(
            '确认',
            `<p>${message}</p>`,
            {
                size: 'modal-sm',
                onConfirm,
                onCancel,
                confirmText: '确定',
                cancelText: '取消'
            }
        );
    },

    /**
     * 提示对话框
     * @param {string} message 消息
     * @param {string} type 类型
     */
    alert(message, type = 'info') {
        const iconMap = {
            success: 'fas fa-check-circle text-success',
            danger: 'fas fa-exclamation-triangle text-danger',
            warning: 'fas fa-exclamation-circle text-warning',
            info: 'fas fa-info-circle text-info'
        };

        return this.show(
            '提示',
            `<div class="d-flex align-items-center">
                <i class="${iconMap[type]} fa-2x me-3"></i>
                <p class="mb-0">${message}</p>
            </div>`,
            {
                size: 'modal-sm',
                showCancel: false,
                confirmText: '确定'
            }
        );
    }
};

// 主题系统
const Theme = {
    /**
     * 切换主题
     * @param {string} theme 主题名称
     */
    switch(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);

        // 触发主题切换事件
        window.dispatchEvent(new CustomEvent('themeChange', { detail: { theme } }));
    },

    /**
     * 获取当前主题
     * @returns {string} 当前主题
     */
    getCurrent() {
        return localStorage.getItem('theme') || 'dark';
    },

    /**
     * 初始化主题
     */
    init() {
        const savedTheme = this.getCurrent();
        this.switch(savedTheme);
    }
};

// 页面初始化
document.addEventListener('DOMContentLoaded', function () {
    // 初始化主题
    Theme.init();

    // 添加CSS样式
    const style = document.createElement('style');
    style.textContent = `
        .notification-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            max-width: 400px;
        }

        .notification {
            background: var(--card-bg);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border-color);
            border-radius: var(--radius-md);
            padding: var(--spacing-md);
            margin-bottom: var(--spacing-sm);
            box-shadow: var(--shadow-lg);
            transition: var(--transition-normal);
        }

        .notification-content {
            display: flex;
            align-items: center;
            gap: var(--spacing-sm);
        }

        .notification-close {
            background: none;
            border: none;
            color: var(--text-secondary);
            cursor: pointer;
            padding: 0;
            margin-left: auto;
        }

        .notification-close:hover {
            color: var(--text-primary);
        }

        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            backdrop-filter: blur(5px);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 9999;
        }

        .loading-content {
            background: var(--card-bg);
            backdrop-filter: blur(20px);
            border: 1px solid var(--border-color);
            border-radius: var(--radius-lg);
            padding: var(--spacing-xl);
            text-align: center;
        }

        .loading-message {
            margin-top: var(--spacing-md);
            color: var(--text-secondary);
        }

        .fade-in {
            animation: fadeIn 0.3s ease-out;
        }

        .fade-out {
            animation: fadeOut 0.3s ease-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        @keyframes fadeOut {
            from { opacity: 1; transform: translateY(0); }
            to { opacity: 0; transform: translateY(-10px); }
        }

        .is-invalid {
            border-color: var(--danger-gradient) !important;
        }

        .invalid-feedback {
            display: block;
            color: #fa709a;
            font-size: var(--font-size-sm);
            margin-top: var(--spacing-xs);
        }

        .empty-state {
            padding: var(--spacing-xl);
            text-align: center;
            color: var(--text-secondary);
        }

        .sortable {
            cursor: pointer;
            user-select: none;
        }

        .sortable:hover {
            background: rgba(255, 255, 255, 0.1);
        }

        .sort-asc::after {
            content: ' ▲';
            color: var(--text-secondary);
        }

        .sort-desc::after {
            content: ' ▼';
            color: var(--text-secondary);
        }
    `;
    document.head.appendChild(style);
});

// 导出全局对象
window.EMS = {
    Utils,
    Notification,
    Loading,
    Animation,
    Validation,
    Table,
    Modal,
    Theme,
    CONFIG
};

