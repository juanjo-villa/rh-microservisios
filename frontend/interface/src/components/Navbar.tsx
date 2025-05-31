
import React from "react";
import { useAuth } from "@/context/AuthContext";
import { Button } from "@/components/ui/button";
import { SidebarTrigger } from "@/components/ui/sidebar";
import { LogOut, User } from "lucide-react";

export const Navbar: React.FC = () => {
  const { user, logout } = useAuth();
  
  return (
    <header className="h-16 border-b flex items-center justify-between px-4 bg-white">
      <div className="flex items-center gap-4">
        <SidebarTrigger className="md:hidden" />
        <h1 className="text-xl font-bold text-primary">WorkWise Enterprise</h1>
      </div>
      
      {user && (
        <div className="flex items-center gap-2">
          <div className="hidden md:flex items-center gap-2">
            <div className="flex items-center gap-2 bg-secondary rounded-full px-3 py-1">
              <User size={16} className="text-muted-foreground" />
              <span className="text-sm font-medium">{user.name}</span>
            </div>
          </div>
          
          <Button 
            variant="ghost" 
            size="icon"
            onClick={logout}
            aria-label="Logout"
          >
            <LogOut size={18} />
          </Button>
        </div>
      )}
    </header>
  );
};

export default Navbar;
