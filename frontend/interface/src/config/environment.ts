// src/environment.ts
export const environment = {
    // Configuración para desarrollo local
    local: {
        employeeApi: 'http://localhost:8005',
        authApi: 'http://localhost:8005',
        payrollApi: 'http://localhost:8006',
        performanceApi: 'http://localhost:8007',
        scheduleApi: 'http://localhost:8008'
    },
    // Configuración para producción (Docker)
    docker: {
        // Use NGINX as API gateway in production
        employeeApi: '/api/employee',
        authApi: '/api/auth',
        payrollApi: '/api/payroll',
        performanceApi: '/evaluations',
        scheduleApi: '/api/schedule'
    }
} as const;

const currentMode = import.meta.env.MODE;
// Selección automática basada en el entorno
export const apiConfig = currentMode === 'production'
    ? environment.docker
    : environment.local;
