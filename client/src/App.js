import './App.css';
import LoginHeader from './components/LoginHeader';
import styled from 'styled-components';

function App() {
  return (
    <AppWrap>
      <LoginHeader />
    </AppWrap>
  );
}

const AppWrap = styled.div`
  text-align: center;
  margin: 50px auto;
`;
export default App;
