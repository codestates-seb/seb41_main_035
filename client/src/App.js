// import React from 'react';
import './App.css';
import LoginHeader from './components/LoginHeader';
import Outer from './pages/Outer';
import Top from './pages/Top';
import Bottom from './pages/Bottom';
import Onepiece from './pages/Onepiece';
import Hat from './pages/Hat';
import Shoes from './pages/Shoes';
import Home from './pages/Home';
import Profile from './pages/Profile';
import Footer from './components/Footer';
import PostUpload from './pages/PostUpload';
import PostView from './pages/PostView';
import Google from './pages/Google';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';

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
          <Route path="/outer" element={<Outer />} />
          <Route path="/top" element={<Top />} />
          <Route path="/bottom" element={<Bottom />} />
          <Route path="/onepiece" element={<Onepiece />} />
          <Route path="/hat" element={<Hat />} />
          <Route path="/shoes" element={<Shoes />} />
          <Route path="/postupload" element={<PostUpload />} />
          <Route path="/postview" element={<PostView />} />
          <Route path="/google" element={<Google />} />
        </Routes>
        <Footer />
      </BrowserRouter>
    </div>
  );
}
export default App;
