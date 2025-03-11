import "./App.css";
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/common/Navbar";
import FooterComponent from "./components/common/Footer";
import HomePage from "./components/home/HomePage";
import AllRoomsPage from "./components/booking_rooms/AllRoomsPage";
import FindBookingPage from "./components/booking_rooms/FindBookingPage";
import RoomDetailsPage from "./components/booking_rooms/RoomDetailsPage";
import { ProtectedRoute, AdminRoute } from "./service/gaurd";
import LoginPage from "./components/auth/LoginPage";
import RegisterPage from "./components/auth/RegisterPage";
import ProfilePage from "./components/profile/ProfilePage";
import EditProfilePage from "./components/profile/EditProfilePage";
import AdminPage from "./components/admin/AdminPage";
import ManageRoomPage from "./components/admin/ManageRoomPage";
import ManageBookingsPage from "./components/admin/ManageBookingsPage";
import AddRoomPage from "./components/admin/AddRoomPage";
import EditRoomPage from "./components/admin/EditRoomPage";
import EditBookingPage from "./components/admin/EditBookingPage";
import Maps from "./components/admin/AnalyseBookings";

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar />
        <div className="content">
          <Routes>
            {/* Public routes */}
            <Route exact path="/home" element={<HomePage />} />
            <Route exact path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route exact path="/rooms" element={<AllRoomsPage />} />
            <Route path="/find-bookings" element={<FindBookingPage />} />

            {/* Authenticated users route*/}
            <Route
              path="/room-details-book/:roomId"
              element={<ProtectedRoute element={<RoomDetailsPage />} />}
            />
            <Route
              path="/profile"
              element={<ProtectedRoute element={<ProfilePage />} />}
            />
            <Route
              path="/edit-profile"
              element={<ProtectedRoute element={<EditProfilePage />} />}
            />

            {/* Admin Auth Router*/}

            <Route
              path="/admin"
              element={<AdminRoute element={<AdminPage />} />}
            />
            <Route
              path="/admin/add-room"
              element={<AdminRoute element={<AddRoomPage />} />}
            />
            <Route
              path="/admin/manage-rooms"
              element={<AdminRoute element={<ManageRoomPage />} />}
            />
            <Route
              path="admin/edit-room/:roomId"
              element={<AdminRoute element={<EditRoomPage />} />}
            />
            <Route
              path="/admin/manage-bookings"
              element={<AdminRoute element={<ManageBookingsPage />} />}
            />

            <Route
              path="/admin/edit-booking/:bookingCode"
              element={<AdminRoute element={<EditBookingPage />} />}
            />
            <Route
              path="/admin/analyse-booking"
              element={<AdminRoute element={<Maps />} />}
            />
          </Routes>
        </div>
        {/* <FooterComponent /> */}
      </div>
    </BrowserRouter>
  );
}

export default App;
