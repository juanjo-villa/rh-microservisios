import axios from "axios";
import { toast } from "@/components/ui/sonner";

import {apiConfig, environment} from "@/config/environment";

// URLs por microservicio (obtenidas de la configuración de entorno)
const BASE_URLS = {
  EMPLOYEE: apiConfig.employeeApi,
  AUTH: apiConfig.authApi,
  PAYROLL: apiConfig.payrollApi,
  PERFORMANCE: apiConfig.performanceApi,
  SCHEDULE: apiConfig.scheduleApi,
};

// Crear una instancia de Axios con configuración predeterminada
const api = axios.create({
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000, // 10 segundos de timeout
});

// Interceptores de solicitud: Agregar token de autorización
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptores de respuesta: Manejo de errores
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error);

    // Check if the error is a network error (no response) or timeout
    if (!error.response) {
      console.error("Network Error - No response from server");

      // Check if it's a timeout error
      if (error.code === 'ECONNABORTED') {
        toast.error("Tiempo de espera agotado", {
          description: "La solicitud ha excedido el tiempo de espera. Verifica que los servicios backend estén en ejecución y respondan correctamente."
        });
      } else {
        toast.error("Error de red", {
          description: "No fue posible conectar con el servidor. Verifica que los servicios backend estén en ejecución."
        });
      }

      // For login page, we don't want to redirect
      if (window.location.pathname !== "/") {
        // Redirect to login page after a delay to show the toast
        setTimeout(() => {
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          window.location.href = "/";
        }, 3000);
      }

      return Promise.reject(error);
    }

    const { response } = error;

    switch (response.status) {
      case 401:
        toast.error("Error de autenticación", {
          description: "Tu sesión ha expirado. Por favor, inicia sesión nuevamente."
        });
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        window.location.href = "/";
        break;
      case 403:
        toast.error("Acceso denegado", {
          description: "No tienes permisos para realizar esta acción."
        });
        break;
      case 500:
        toast.error("Error del servidor", {
          description: "Ocurrió un problema interno. Intenta nuevamente más tarde."
        });
        break;
      default:
        toast.error("Error en la solicitud", {
          description: response.data?.message || "Ocurrió un error."
        });
    }

    return Promise.reject(error);
  }
);

// Funciones de servicios API para cada microservicio

// Auth API
interface LoginCredentials {
  email: string;
  password: string;
}

interface JwtResponse {
  token: string;
  type: string;
  id: string;
  username: string;
}

export const authApi = {
  login: (credentials: LoginCredentials) => 
    api.post<JwtResponse>(`${BASE_URLS.AUTH}/login`, credentials),
};

// Employee API 
interface Employee {
  id?: number;
  dni: string;
  name: string;
  lastName: string;
  address: string;
  email: string;
  phone: string;
  password: string;
  positionId: number;
}

export const employeeApi = {
  getAll: () => api.get<Employee[]>(`${BASE_URLS.EMPLOYEE}/api/employee`),
  getById: (id: number) => api.get<Employee>(`${BASE_URLS.EMPLOYEE}/api/employee/${id}`),
  getByEmail: (email: string) => api.get<Employee>(`${BASE_URLS.EMPLOYEE}/api/employee/email/${email}`),
  create: (employee: Employee) => api.post<Employee>(`${BASE_URLS.EMPLOYEE}/api/employee`, employee),
  update: (id: number, employee: Partial<Employee>) =>
      api.put<Employee>(`${BASE_URLS.EMPLOYEE}/api/employee/${id}`, employee),
  delete: (id: number) => api.delete(`${BASE_URLS.EMPLOYEE}/api/employee/${id}`),
};

// Position API
interface Position {
  id?: number;
  name: string;
  description: string;
  salary: number;
}

export const positionApi = {
  getAll: () => api.get<Position[]>(`${BASE_URLS.EMPLOYEE}/api/position`),
  getById: (id: number) => api.get<Position>(`${BASE_URLS.EMPLOYEE}/api/position/${id}`),
  getByName: (name: string) => api.get<Position>(`${BASE_URLS.EMPLOYEE}/api/position/name/${name}`),
  create: (position: Position) => api.post<Position>(`${BASE_URLS.EMPLOYEE}/api/position`, position),
  update: (id: number, position: Partial<Position>) =>
      api.put<Position>(`${BASE_URLS.EMPLOYEE}/api/position/${id}`, position),
  delete: (id: number) => api.delete(`${BASE_URLS.EMPLOYEE}/api/position/${id}`),
};

// Status API
interface Status {
  id?: number;
  type: string;
  startDate: Date;
  endDate: Date;
  paid: number;
  description: string;
  employeeId: number;
  statusPermissionId: number;
}

export const statusApi = {
  getAll: () => api.get<Status[]>(`${BASE_URLS.EMPLOYEE}/api/status`),
  getById: (id: number) => api.get<Status>(`${BASE_URLS.EMPLOYEE}/api/status/${id}`),
  create: (status: Status) => api.post<Status>(`${BASE_URLS.EMPLOYEE}/api/status`, status),
  update: (id: number, status: Partial<Status>) =>
      api.put<Status>(`${BASE_URLS.EMPLOYEE}/api/status/${id}`, status),
  delete: (id: number) => api.delete(`${BASE_URLS.EMPLOYEE}/api/status/${id}`),
};

// Payroll API
interface Payroll {
  id: number;
  employeeId: number;
  amount: number;
  date: string;
}

interface PayrollAdjustment {
  id?: number;
  amount: number;
  reason: string;
}

export const payrollApi = {
  getAll: () => api.get<Payroll[]>(`${BASE_URLS.PAYROLL}/api/payrolls`),
  getById: (id: number) => api.get<Payroll>(`${BASE_URLS.PAYROLL}/api/payrolls/${id}`),
  getByEmployee: (employeeId: number) =>
      api.get<Payroll[]>(`${BASE_URLS.PAYROLL}/api/employees/${employeeId}/payrolls`),
  createAdjustment: (payrollId: number, adjustment: PayrollAdjustment) =>
      api.post<PayrollAdjustment>(`${BASE_URLS.PAYROLL}/api/payrolls/${payrollId}/adjustments`, adjustment),
  deleteAdjustment: (payrollId: number, adjustmentId: number) =>
      api.delete(`${BASE_URLS.PAYROLL}/api/payrolls/${payrollId}/adjustments/${adjustmentId}`),
};

// Performance API
interface Evaluation {
  id?: number;
  employeeId: number;
  score: number;
  comments: string;
  date: string;
}

export const performanceApi = {
  getAll: () => api.get<Evaluation[]>(`${BASE_URLS.PERFORMANCE}/evaluations`),
  getById: (id: number) => api.get<Evaluation>(`${BASE_URLS.PERFORMANCE}/evaluations/${id}`),
  create: (evaluation: Omit<Evaluation, 'id'>) =>
      api.post<Evaluation>(`${BASE_URLS.PERFORMANCE}/evaluations`, evaluation),
  update: (id: number, evaluation: Partial<Omit<Evaluation, 'id'>>) =>
      api.put<Evaluation>(`${BASE_URLS.PERFORMANCE}/evaluations/${id}`, evaluation),
  delete: (id: number) => api.delete(`${BASE_URLS.PERFORMANCE}/evaluations/${id}`),
  getByEmployee: (employeeId: number) =>
      api.get<Evaluation[]>(`${BASE_URLS.PERFORMANCE}/evaluations/employee/${employeeId}`),
};

// Alias for performanceApi to maintain compatibility with components using evaluationApi
export const evaluationApi = performanceApi;

// Role API
interface Role {
  id?: number;
  name: string;
  description: string;
  salary: number;
  employeeCount?: number;
}

export const roleApi = {
  getAll: () => api.get<Role[]>(`${BASE_URLS.EMPLOYEE}/api/role`),
  getById: (id: number) => api.get<Role>(`${BASE_URLS.EMPLOYEE}/api/role/${id}`),
  create: (role: Omit<Role, 'id'>) => api.post<Role>(`${BASE_URLS.EMPLOYEE}/api/role`, role),
  update: (id: number, role: Partial<Omit<Role, 'id'>>) =>
      api.put<Role>(`${BASE_URLS.EMPLOYEE}/api/role/${id}`, role),
  delete: (id: number) => api.delete(`${BASE_URLS.EMPLOYEE}/api/role/${id}`),
};

// Schedule API
interface Schedule {
  id?: number;
  date: string;
  startTime: string;
  exitTime: string;
  totalHours: number;
  deductedHours: number;
  employeeIds?: number[];
}

interface EmployeeSchedule {
  id?: number;
  employeeId: number;
  scheduleId: number;
}

interface CountEmployeeSchedule {
  id?: number;
  employeeScheduleId: number;
  workDate: string;
  workHours: number;
}

export const scheduleApi = {
  // Schedule endpoints
  getAll: () => api.get<Schedule[]>(`${BASE_URLS.SCHEDULE}/api/schedules`),
  getById: (id: number) => api.get<Schedule>(`${BASE_URLS.SCHEDULE}/api/schedules/${id}`),
  getByDate: (date: string) => api.get<Schedule>(`${BASE_URLS.SCHEDULE}/api/schedules/date/${date}`),
  create: (schedule: Omit<Schedule, 'id'>) =>
      api.post<Schedule>(`${BASE_URLS.SCHEDULE}/api/schedules`, schedule),
  createSimple: (schedule: Omit<Schedule, 'id'>) =>
      api.post<Schedule>(`${BASE_URLS.SCHEDULE}/api/schedules/simple`, schedule),
  update: (id: number, schedule: Partial<Omit<Schedule, 'id'>>) =>
      api.put<Schedule>(`${BASE_URLS.SCHEDULE}/api/schedules/${id}`, schedule),
  delete: (id: number) => api.delete(`${BASE_URLS.SCHEDULE}/api/schedules/${id}`),

  // Employee Schedule endpoints
  getAllEmployeeSchedules: () => 
      api.get<EmployeeSchedule[]>(`${BASE_URLS.SCHEDULE}/api/employee-schedule`),
  getEmployeeScheduleById: (id: number) => 
      api.get<EmployeeSchedule>(`${BASE_URLS.SCHEDULE}/api/employee-schedule/${id}`),
  getEmployeeScheduleByEmployeeAndSchedule: (employeeId: number, scheduleId: number) => 
      api.get<EmployeeSchedule>(`${BASE_URLS.SCHEDULE}/api/employee-schedule/employee/${employeeId}/schedule/${scheduleId}`),
  createEmployeeSchedule: (employeeSchedule: Omit<EmployeeSchedule, 'id'>) => 
      api.post<EmployeeSchedule>(`${BASE_URLS.SCHEDULE}/api/employee-schedule/new-employee-schedule`, employeeSchedule),
  deleteEmployeeSchedule: (id: number) => 
      api.delete(`${BASE_URLS.SCHEDULE}/api/employee-schedule/${id}`),

  // Count Employee Schedule endpoints (time tracking)
  getAllCountEmployeeSchedules: () => 
      api.get<CountEmployeeSchedule[]>(`${BASE_URLS.SCHEDULE}/api/count-schedule`),
  getCountEmployeeScheduleById: (id: number) => 
      api.get<CountEmployeeSchedule>(`${BASE_URLS.SCHEDULE}/api/count-schedule/${id}`),
  createCountEmployeeSchedule: (countEmployeeSchedule: Omit<CountEmployeeSchedule, 'id'>) => 
      api.post<CountEmployeeSchedule>(`${BASE_URLS.SCHEDULE}/api/count-schedule`, countEmployeeSchedule),
  updateCountEmployeeSchedule: (id: number, countEmployeeSchedule: Partial<Omit<CountEmployeeSchedule, 'id'>>) => 
      api.put<CountEmployeeSchedule>(`${BASE_URLS.SCHEDULE}/api/count-schedule/${id}`, countEmployeeSchedule),
  deleteCountEmployeeSchedule: (id: number) => 
      api.delete(`${BASE_URLS.SCHEDULE}/api/count-schedule/${id}`),
  registerHours: (employeeId: number, hours: number) => 
      api.post<void>(`${BASE_URLS.SCHEDULE}/api/count-schedule/${employeeId}?hours=${hours}`),
};

export default api;
