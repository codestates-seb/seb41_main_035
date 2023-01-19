import styled from 'styled-components';
import { useState } from 'react';
import LoginModal from './LoginModal/LoginModal';
import { BsPersonCircle, BsPencilSquare } from 'react-icons/bs';
import { AiOutlineMessage } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import SearchBox from './SearchBox';
import ChattingList from './ChattingList';
import userStore from '../store/userStore';
const BREAK_POINT_PC = 1300;
const BREAK_POINT_TABLET = 768;

const LoginHeader = () => {
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);
  const userId = userStore((state) => state.userId);

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
          <div className="header-container">
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
              <BsPersonCircle
                onClick={() => navigate(`/profile/${userId}`)}
                size="30"
              />
              <BsPencilSquare
                size="30"
                role="presentation"
                onClick={() => navigate(`/postupload`)}
              />
              <AiOutlineMessage size="30" onClick={onChatOpen} />
              <button className="login button">Log out</button>
            </div>
            {/* ) : ( */}
            {/* <div className="right zone">
              <button className="login button" onClick={onClickButton}>
                Log in
              </button>
            </div> */}
            {/* )} */}

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
  height: 12vh;
  border: 3px solid #196ba5;
  border-top: 0;
  border-left: 0;
  border-right: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  .header-container {
    display: flex;
    width: 76%;
    height: 12vh;
    align-items: center;
    justify-content: space-between;
  }
  .title {
    flex-grow: 1;
    font-size: 50px;
    cursor: pointer;
    /* margin-left: 30px; */
    margin-top: 70px;
    font-family: 'Song Myung', serif;
    color: #196ba5;
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      & {
        font-size: 40px;
      }
    }
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        font-size: 20px;
      }
    }
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
