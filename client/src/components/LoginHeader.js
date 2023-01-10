import styled from 'styled-components';
import { useState } from 'react';
import LoginModal from './LoginModal/LoginModal';

const LoginHeader = () => {
  const [isOpen, setIsOpen] = useState(false);

  const onClickButton = () => {
    setIsOpen(true);
  };

  return (
    <>
      <SWrapper>
        <SHeader>
          <p className="title">Look at me</p>
          <form>
            <input
              className="header-bottom-search"
              type="search"
              placeholder="브랜드명, 상품명으로 검색"
            ></input>
          </form>
          <button className="login button" onClick={onClickButton}>
            로그인
          </button>
        </SHeader>
        {isOpen && (
          <LoginModal
            open={isOpen}
            onClose={() => {
              setIsOpen(false);
            }}
          />
        )}
      </SWrapper>
    </>
  );
};
const SWrapper = styled.div`
  position: sticky;
  top: 0;
  z-index: 5;
  justify-content: center;
  display: flex;
`;
const SHeader = styled.div`
  box-sizing: border-box;
  background-color: #d9d9d9;
  width: 70%;
  height: 18vh;
  display: flex;
  justify-content: space-around;
  align-items: center;
  .title {
    font-size: 60px;
    margin-left: -45px;
  }
  input {
    width: 35vw;
    height: 5vh;
    border: 2px solid black;
    margin-top: 15px;
    margin-left: -70px;
    background: transparent;
  }
  button {
    width: 5vw;
    height: 30px;
    margin-left: -50px;
    margin-top: 15px;
    background-color: #f67b7b;
    color: white;
  }
`;

export default LoginHeader;
