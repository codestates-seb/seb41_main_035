// import React from 'react';
import './App.css';
import LoginHeader from './components/LoginHeader';
import Home from './pages/Home';
import Profile from './pages/Profile';
import Footer from './components/Footer';
import PostUpload from './pages/PostUpload';
import PostView from './pages/PostView';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Category from './pages/Category';
function App() {
  return (
    <div>
      <BrowserRouter>
        <LoginHeader />
        <Sidebar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/profile/:userId" element={<Profile />} />
          <Route path="/post/" element={<PostUpload />} />
          <Route path="/postupload" element={<PostUpload />} />
          <Route path="/postview" element={<PostView />} />
          <Route path="/category/:categoryId" element={<Category />} />
        </Routes>
        <Footer />
      </BrowserRouter>
    </div>
  );
}
export default App;
