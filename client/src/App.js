// import React from 'react';
import './App.css';
import PostUpload from './pages/PostUpload';
import PostView from './pages/PostView';

import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          {/* <Route path="/" element={<Home />} /> */}
          {/* 게시글하나 클릭시 나오는 페이지 */}
          <Route path="/postview" element={<PostView />} />
        </Routes>
        <PostUpload />
        <br />
        <PostView />
        <br />
      </BrowserRouter>
    </>
  );
}

export default App;
