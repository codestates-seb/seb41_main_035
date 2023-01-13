import styled from 'styled-components';
import { useState } from 'react';
import LoginModal from './LoginModal/LoginModal';
import { BsPersonCircle, BsPencilSquare } from 'react-icons/bs';
import { AiOutlineMessage } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import SearchBox from './SearchBox';

const LoginHeader = () => {
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);

  const onClickButton = () => {
    setIsOpen(true);
  };

  return (
    <>
      <SWrapper>
        <SHeader>
          <p
            className="title"
            role="presentation"
            onClick={() => navigate(`/`)}
          >
            Look at me
          </p>
          <SearchBox />
          <div className="right zone">
            <BsPersonCircle size="30" />
            <BsPencilSquare size="30" />
            <AiOutlineMessage size="30" />
            <button className="login button" onClick={onClickButton}>
              로그인
            </button>
          </div>
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
  background-color: #d9d9d9;
  width: 100%;
  height: 15vh;
  display: flex;
  align-items: center;
  border: 1px solid gray;
  border-top: 0;
  border-left: 0;
  border-right: 0;

  .title {
    text-align: center;
    flex-grow: 1;
    font-size: 60px;
    cursor: pointer;
  }

  .right {
    flex-grow: 1;
    display: flex;
    justify-content: center;

    button {
      width: 5vw;
      height: 30px;
      margin-top: 15px;
      background-color: #f67b7b;
      color: white;
      cursor: pointer;
    }
    svg {
      margin-top: 15px;
      padding: 0 20px;
    }
  }
`;

export default LoginHeader;
