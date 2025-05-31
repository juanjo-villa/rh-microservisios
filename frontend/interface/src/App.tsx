
import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "@/context/AuthContext";
import { PrivateRoute } from "@/routes/PrivateRoute";

// Auth Pages
import LoginPage from "@/pages/Auth/LoginPage";
import NotFound from "@/pages/NotFound";

// Admin Pages
import AdminDashboard from "@/pages/Admin/Dashboard";
import EmployeeList from "@/pages/Admin/Employees/EmployeeList";
import EmployeeForm from "@/pages/Admin/Employees/EmployeeForm";
import RoleList from "@/pages/Admin/Roles/RoleList";
import RoleForm from "@/pages/Admin/Roles/RoleForm";
import StatusList from "@/pages/Admin/Status/StatusList";
import StatusForm from "@/pages/Admin/Status/StatusForm";
import ScheduleList from "@/pages/Admin/Schedules/ScheduleList";
import ScheduleForm from "@/pages/Admin/Schedules/ScheduleForm";
import EvaluationList from "@/pages/Admin/Evaluations/EvaluationList";
import EvaluationForm from "@/pages/Admin/Evaluations/EvaluationForm";
import PayrollView from "@/pages/Admin/Payroll/PayrollView";
import RequestList from "@/pages/Admin/Requests/RequestList";

// Employee Pages
import EmployeeProfile from "@/pages/Employee/Profile";
import MySchedule from "@/pages/Employee/MySchedule";
import MyPayroll from "@/pages/Employee/MyPayroll";
import MyEvaluations from "@/pages/Employee/MyEvaluations";
import MyRequests from "@/pages/Employee/MyRequests";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<LoginPage />} />

            {/* Admin Routes */}
            <Route element={<PrivateRoute allowedRoles={["admin"]} />}>
              <Route path="/admin/dashboard" element={<AdminDashboard />} />

              {/* Employee Routes */}
              <Route path="/admin/employees" element={<EmployeeList />} />
              <Route path="/admin/employees/new" element={<EmployeeForm />} />
              <Route path="/admin/employees/:id/edit" element={<EmployeeForm />} />

              {/* Role Routes */}
              <Route path="/admin/roles" element={<RoleList />} />
              <Route path="/admin/roles/new" element={<RoleForm />} />
              <Route path="/admin/roles/:id/edit" element={<RoleForm />} />

              {/* Status Routes */}
              <Route path="/admin/status" element={<StatusList />} />
              <Route path="/admin/status/new" element={<StatusForm />} />
              <Route path="/admin/status/:id/edit" element={<StatusForm />} />

              {/* Schedule Routes */}
              <Route path="/admin/schedules" element={<ScheduleList />} />
              <Route path="/admin/schedules/new" element={<ScheduleForm />} />
              <Route path="/admin/schedules/:id/edit" element={<ScheduleForm />} />

              {/* Evaluation Routes */}
              <Route path="/admin/evaluations" element={<EvaluationList />} />
              <Route path="/admin/evaluations/new" element={<EvaluationForm />} />
              <Route path="/admin/evaluations/:id/edit" element={<EvaluationForm />} />

              <Route path="/admin/payroll" element={<PayrollView />} />
              <Route path="/admin/requests" element={<RequestList />} />
            </Route>

            {/* Employee Routes */}
            <Route element={<PrivateRoute allowedRoles={["employee"]} />}>
              <Route path="/employee/profile" element={<EmployeeProfile />} />
              <Route path="/employee/schedule" element={<MySchedule />} />
              <Route path="/employee/payroll" element={<MyPayroll />} />
              <Route path="/employee/evaluations" element={<MyEvaluations />} />
              <Route path="/employee/requests" element={<MyRequests />} />
            </Route>

            {/* Catch-all route */}
            <Route path="*" element={<NotFound />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
