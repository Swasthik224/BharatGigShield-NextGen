import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import UserDashboard from './pages/UserDashboard';
import Dashboard from './pages/Dashboard';
import Triggers  from './pages/Triggers';
import Pools     from './pages/Pools';
import Actuarial from './pages/Actuarial';
import Login     from './pages/Login';
import Layout    from './components/Layout';

const qc = new QueryClient({
  defaultOptions: { queries: { retry: 1, staleTime: 30000 } }
});

// 🔐 Check login
const PrivateRoute = ({ children }) => {
  return localStorage.getItem('jwt_token')
    ? children
    : <Navigate to="/login" replace />;
};

// 👨‍💼 Admin access
const AdminRoute = ({ children }) => {
  const role = localStorage.getItem('role');

  if (role === 'ADMIN') return children;

  return <Navigate to="/login" replace />; // ✅ FIXED
};

// 👤 User access
const UserRoute = ({ children }) => {
  const role = localStorage.getItem('role');

  if (role === 'USER') return children;

  return <Navigate to="/login" replace />; // ✅ FIXED
};

export default function App() {
  return (
    <QueryClientProvider client={qc}>
      <BrowserRouter>
        <Routes>

          {/* LOGIN */}
          <Route path="/login" element={<Login />} />

          {/* ADMIN ROUTES */}
          <Route
            path="/admin"
            element={
              <PrivateRoute>
                <AdminRoute>
                  <Layout />
                </AdminRoute>
              </PrivateRoute>
            }
          >
            <Route index element={<Dashboard />} />
            <Route path="triggers"  element={<Triggers />} />
            <Route path="pools"     element={<Pools />} />
            <Route path="actuarial" element={<Actuarial />} />
          </Route>

          {/* USER ROUTE */}
          <Route
            path="/user"
            element={
              <PrivateRoute>
                <UserRoute>
                  <UserDashboard />
                </UserRoute>
              </PrivateRoute>
            }
          />

          {/* ROOT REDIRECT (SMART) */}
          <Route
            path="/"
            element={
              localStorage.getItem("role") === "ADMIN"
                ? <Navigate to="/admin" />
                : localStorage.getItem("role") === "USER"
                ? <Navigate to="/user" />
                : <Navigate to="/login" />
            }
          />

        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  );
}