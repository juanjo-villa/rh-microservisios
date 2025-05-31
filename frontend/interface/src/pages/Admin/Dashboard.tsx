
import React from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Users, Briefcase, Clock, Award, DollarSign, Mail } from "lucide-react";
import { Link } from "react-router-dom";

const Dashboard: React.FC = () => {
  // Mock dashboard stats data (would come from API in real app)
  const stats = [
    { 
      title: "Total Employees", 
      value: 127, 
      icon: Users, 
      link: "/admin/employees",
      color: "bg-blue-100 text-blue-600" 
    },
    { 
      title: "Roles", 
      value: 15, 
      icon: Briefcase, 
      link: "/admin/roles",
      color: "bg-purple-100 text-purple-600" 
    },
    { 
      title: "Active Schedules", 
      value: 28, 
      icon: Clock, 
      link: "/admin/schedules",
      color: "bg-green-100 text-green-600" 
    },
    { 
      title: "Pending Evaluations", 
      value: 18, 
      icon: Award, 
      link: "/admin/evaluations",
      color: "bg-yellow-100 text-yellow-600" 
    },
    { 
      title: "Payroll This Month", 
      value: "$156,432", 
      icon: DollarSign, 
      link: "/admin/payroll",
      color: "bg-emerald-100 text-emerald-600" 
    },
    { 
      title: "Pending Requests", 
      value: 7, 
      icon: Mail, 
      link: "/admin/requests",
      color: "bg-red-100 text-red-600" 
    },
  ];

  // Recent employee activity (would come from API in real app)
  const recentActivity = [
    { name: "Alice Johnson", action: "requested time off", time: "2 hours ago" },
    { name: "Bob Smith", action: "completed evaluation", time: "5 hours ago" },
    { name: "Carol Williams", action: "checked in", time: "Today, 8:05 AM" },
    { name: "David Brown", action: "submitted expense report", time: "Yesterday" },
  ];

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Admin Dashboard</h1>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {stats.map((stat, index) => (
          <Link to={stat.link} key={index}>
            <Card className="hover:border-primary/50 transition-colors">
              <CardHeader className="pb-2">
                <CardTitle className="text-base font-medium">{stat.title}</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex justify-between items-center">
                  <span className="text-2xl font-bold">{stat.value}</span>
                  <div className={`p-2 rounded-full ${stat.color}`}>
                    <stat.icon size={20} />
                  </div>
                </div>
              </CardContent>
            </Card>
          </Link>
        ))}
      </div>

      <div className="mt-8">
        <Card>
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
            <CardDescription>Latest actions from employees</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {recentActivity.map((activity, index) => (
                <div key={index} className="flex items-center gap-3 p-2 rounded-md hover:bg-muted">
                  <div className="flex h-9 w-9 items-center justify-center rounded-full bg-primary/10">
                    <Users size={18} className="text-primary" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm font-medium">
                      <span className="font-semibold">{activity.name}</span> {activity.action}
                    </p>
                    <p className="text-xs text-muted-foreground">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default Dashboard;
