// import React from 'react';
import './App.css';
import LoginHeader from './components/LoginHeader';
import Home from './pages/Home';
import Profile from './pages/Profile';
import Footer from './components/Footer';
import PostUpload from './pages/PostUpload';
import PostView from './pages/PostView';
import Google from './pages/Google';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Category from './pages/Category';
import About from './pages/About';
import Chat from './pages/Chat';
// import EditPost from './pages/EditPost';
function App() {
  return (
    <div>
      <BrowserRouter>
        <LoginHeader />
        <Sidebar />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/profile/:userId" element={<Profile />} />
          <Route path="/postupload" element={<PostUpload />} />
          <Route path="/postview/:boardId" element={<PostView />} />
          <Route path="/google" element={<Google />} />
          <Route path="/category/:categoryId" element={<Category />} />
          <Route path="/about" element={<About />} />
          <Route path="/chatting" element={<Chat />} />
          {/* <Route path="/edit" element={<EditPost />} /> */}
        </Routes>
        <Footer />
      </BrowserRouter>
    </div>
  );
}
export default App;
