// import React from 'react';
import './App.css';
import PostUpload from './pages/PostUpload';
import PostView from './pages/PostView';
import LoginHeader from './components/LoginHeader';
import Outer from './pages/Outer';
import Top from './pages/Top';
import Bottom from './pages/Bottom';
import Onepiece from './pages/Onepiece';
import Hat from './pages/Hat';
import Shoes from './pages/Shoes';
import Home from './pages/Home';
import Footer from './components/Footer';
import ChattingList from './pages/ChattingList';
import ChatWindow from './pages/ChatWindow';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <div>
      <BrowserRouter>
        <LoginHeader />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/outer" element={<Outer />} />
          <Route path="/top" element={<Top />} />
          <Route path="/bottom" element={<Bottom />} />
          <Route path="/onepiece" element={<Onepiece />} />
          <Route path="/hat" element={<Hat />} />
          <Route path="/shoes" element={<Shoes />} />
          <Route path="/chattinglist" element={<ChattingList />} />
          <Route path="/chat" element={<ChatWindow />} />
        </Routes>
        <Footer />
      </BrowserRouter>
    </div>
  );
}
export default App;
