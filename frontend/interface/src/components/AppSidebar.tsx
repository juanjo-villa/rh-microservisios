import React, { useState } from "react";
import { NavLink, useLocation } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarTrigger,
  useSidebar,
} from "@/components/ui/sidebar";
import {
  User,
  Users,
  Briefcase,
  Clock,
  FileText,
  DollarSign,
  Award,
  Mail,
  Home,
} from "lucide-react";

export const AppSidebar: React.FC = () => {
  const { state } = useSidebar(); // Use state instead of collapsed
  const { user } = useAuth();
  const location = useLocation();
  const currentPath = location.pathname;
  
  // Check if sidebar is collapsed
  const collapsed = state === "collapsed";
  
  // Navigation items based on user role
  const adminItems = [
    { title: "Dashboard", url: "/admin/dashboard", icon: Home },
    { title: "Employees", url: "/admin/employees", icon: Users },
    { title: "Roles", url: "/admin/roles", icon: Briefcase },
    { title: "Schedules", url: "/admin/schedules", icon: Clock },
    { title: "Evaluations", url: "/admin/evaluations", icon: Award },
    { title: "Payroll", url: "/admin/payroll", icon: DollarSign },
    { title: "Requests", url: "/admin/requests", icon: Mail },
  ];

  const employeeItems = [
    { title: "My Profile", url: "/employee/profile", icon: User },
    { title: "My Schedule", url: "/employee/schedule", icon: Clock },
    { title: "My Payroll", url: "/employee/payroll", icon: DollarSign },
    { title: "My Evaluations", url: "/employee/evaluations", icon: Award },
    { title: "My Requests", url: "/employee/requests", icon: Mail },
  ];
  
  // Select navigation items based on user role
  const navItems = user?.role === "admin" ? adminItems : employeeItems;

  // Helper functions for active routes
  const isActive = (path: string) => currentPath === path || currentPath.startsWith(`${path}/`);
  const getNavClass = ({ isActive }: { isActive: boolean }) =>
    `w-full flex items-center gap-2 px-3 py-2 rounded-md transition-colors ${
      isActive ? "bg-sidebar-accent text-primary font-medium" : "hover:bg-sidebar-accent/50"
    }`;

  // Check if any nav item is active to determine if we should keep the sidebar group open
  const isGroupExpanded = navItems.some(item => isActive(item.url));

  return (
    <Sidebar
      className={collapsed ? "w-16" : "w-64"}
      collapsible="icon" // Use "icon" instead of boolean true
    >
      {/* Sidebar trigger (visible in mobile/collapsed state) */}
      <SidebarTrigger className="m-2 self-end md:hidden" />

      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel className="uppercase text-xs font-semibold text-muted-foreground">
            {user?.role === "admin" ? "Admin Panel" : "Employee Portal"}
          </SidebarGroupLabel>

          <SidebarGroupContent>
            <SidebarMenu>
              {navItems.map((item) => (
                <SidebarMenuItem key={item.url}>
                  <SidebarMenuButton asChild>
                    <NavLink 
                      to={item.url} 
                      end={item.url.includes("dashboard")} 
                      className={getNavClass}
                    >
                      <item.icon className="h-5 w-5" />
                      {!collapsed && <span>{item.title}</span>}
                    </NavLink>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  );
};

export default AppSidebar;
