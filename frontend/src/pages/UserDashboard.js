import React from "react";

export default function UserDashboard() {
  return (
    <div style={{
      minHeight: "100vh",
      background: "#0f172a",
      color: "white",
      padding: "40px"
    }}>
      <h1>User Dashboard</h1>

      <div style={{
        marginTop: 20,
        background: "#1e293b",
        padding: 20,
        borderRadius: 10
      }}>
        <h3>Welcome to GigShield 👋</h3>
        <p>You are logged in as USER</p>
      </div>

      <div style={{
        marginTop: 20,
        background: "#1e293b",
        padding: 20,
        borderRadius: 10
      }}>
        <h3>Your Features</h3>
        <ul>
          <li>View Policies</li>
          <li>Track Claims</li>
          <li>Check Payouts</li>
        </ul>
      </div>
    </div>
  );
}