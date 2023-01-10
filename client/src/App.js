// import React from 'react';
import './App.css';
import LoginHeader from './components/LoginHeader';
import styled from 'styled-components';

import Home from './pages/Home';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
<div>
    <AppWrap>
      <LoginHeader />
    </AppWrap>

    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
      </Routes>
    </BrowserRouter>
</div>

  );
}

const AppWrap = styled.div`
  text-align: center;
  margin: 50px auto;
`;
export default App;
