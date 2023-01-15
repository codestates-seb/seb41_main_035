import styled from 'styled-components';
import { useState } from 'react';
import LoginModal from './LoginModal/LoginModal';
import { BsPersonCircle, BsPencilSquare } from 'react-icons/bs';
import { AiOutlineMessage } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import SearchBox from './SearchBox';
import Avatar from './Avatar';
import ChattingList from './ChattingList';

const LoginHeader = () => {
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);

  const onClickButton = () => {
    setIsOpen(true);
  };
  const onChatOpen = () => {
    setIsChatOpen((prev) => !prev);
  };

  return (
    <>
      <SWrapper>
        <SHeader>
          {/* <Avatar
            image="스크린샷 2023-01-13 오후 4.47.01 1.png"
            size="54px"
          /> */}
          <p
            className="title"
            role="presentation"
            onClick={() => navigate(`/`)}
          >
            Look at me
          </p>
          {/* className={isLogin ? 'Loginsearch' : 'LogoutSearch'} */}
          <SearchBox />
          {/* {isLogin ? ( */}
          <div className="right zone">
            <BsPersonCircle size="30" />
            <BsPencilSquare size="30" />
            <AiOutlineMessage size="30" onClick={onChatOpen} />
            <button className="login button">Log out</button>
          </div>
          {/* ) : ( */}
          {/* <div className="right zone">
            <BsPersonCircle size="30" />
            <BsPencilSquare size="30" />
            <AiOutlineMessage size="30" onClick={onChatOpen} />
            <button className="login button" onClick={onClickButton}>
              Log in
            </button>
          </div> */}
          {/* )} */}
        </SHeader>
        {isOpen && (
          <LoginModal
            open={isOpen}
            onClose={() => {
              setIsOpen(false);
            }}
          />
        )}
        {isChatOpen && <ChattingList onChatOpen={onChatOpen} />}
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
  width: 100%;
  height: 13vh;
  display: flex;
  align-items: center;
  border: 3px solid #196ba5;
  justify-content: space-between;
  border-top: 0;
  border-left: 0;
  border-right: 0;

  .title {
    /* text-align: center; */
    flex-grow: 1;
    font-size: 60px;
    cursor: pointer;
    margin-left: 20px;
    margin-top: 80px;
    color: #196ba5;
  }
  .autocomplete-wrapper {
    /* position: absolute; */
  }
  .right {
    flex-grow: 1;
    display: flex;
    justify-content: flex-end;
    margin-right: 20px;
    margin-top: 40px;
    svg {
      cursor: pointer;
      color: #565656;
      padding: 0 20px;
    }
    button {
      width: 6vw;
      height: 30px;
      font-size: 17px;
      border: none;
      color: darkgray;
      cursor: pointer;
    }
  }
`;

export default LoginHeader;
