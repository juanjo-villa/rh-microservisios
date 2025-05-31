
import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";
import { AppLayout } from "@/components/Layout";

interface PrivateRouteProps {
  allowedRoles: Array<"admin" | "employee">;
}

export const PrivateRoute: React.FC<PrivateRouteProps> = ({ 
  allowedRoles
}) => {
  const { user, isLoading } = useAuth();
  
  // Show loading state
  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }
  
  // Check if user is authenticated
  if (!user) {
    return <Navigate to="/" replace />;
  }
  
  // Check if user has required role
  if (!allowedRoles.includes(user.role)) {
    return <Navigate to={user.role === "admin" ? "/admin/dashboard" : "/employee/profile"} replace />;
  }
  
  // If the user is authenticated and has the required role, render the outlet
  return (
    <AppLayout>
      <Outlet />
    </AppLayout>
  );
};

export default PrivateRoute;
