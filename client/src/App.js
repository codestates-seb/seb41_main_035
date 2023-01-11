// import React from 'react';
import './App.css';
import LoginHeader from './components/LoginHeader';
import styled from 'styled-components';

import Home from './pages/Home';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
      <LoginHeader />
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
      </Routes>
    </BrowserRouter>
</div>
  );
}
export default App;
