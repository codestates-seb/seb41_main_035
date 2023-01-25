import styled from 'styled-components';
import { useState } from 'react';
import LoginModal from './LoginModal/LoginModal';
import { BsPersonCircle, BsPencilSquare } from 'react-icons/bs';
import { AiOutlineMessage } from 'react-icons/ai';
import { GoThreeBars } from 'react-icons/go';
import { useNavigate } from 'react-router-dom';
import SearchBox from './SearchBox';
import ChattingList from './ChattingList';
import userStore from '../store/userStore';
import memberstore from '../store/memberstore';
import axios from 'axios';
import { BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';
const backendUrl = 'http://13.125.30.88/';


const LoginHeader = () => {
  const { isLogin, setisLogin } = memberstore((state) => state);
  const navigate = useNavigate();
  const [isOpen, setIsOpen] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);
  const userId = userStore((state) => state.userId);

  const onClickButton = () => {
    setIsOpen(true);
  };

  const Logout = async () => {
    const token = localStorage.getItem('accessToken');
    const res = await axios.post(
      `${backendUrl}auth/logout`,
      {},
      {
        headers: { Authorization: token },
      }
    );
    if (res) {
      localStorage.removeItem('accessToken');
      // eslint-disable-next-line react/prop-types
    }
    setisLogin(false);
  };

  const onChatOpen = () => {
    setIsChatOpen((prev) => !prev);
  };

  return (
    <>
      <SWrapper>
        <SHeader>
          <div className="header-container">
            <div className="title-name">
              <p
                className="title"
                role="presentation"
                onClick={() => navigate(`/`)}
              >
                Look at me
              </p>
            </div>
            <SearchBox />
                       {!isLogin ? (
              <div className="right zone">
                <button className="login button" onClick={onClickButton}>
                  Log in
                </button>
              </div>
            ) : (
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
                <button className="logout button" onClick={Logout}>
                  Log out
                </button>
              </div>
            )}
            <GoThreeBars className="menu-bar" />
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
  height: 12vh;
  z-index: 300;
  position: sticky;
`;
const SHeader = styled.div`
  height: 12vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  border-bottom: 3px solid #196ba5;
  background-color: #fff;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    padding: 0 10px;
  }
  .header-container {
    display: flex;
    width: 100%;
    max-width: 1250px;
    align-items: center;
    justify-content: space-between;
  }
  .title-name {
    flex-grow: 4;
    height: 12vh;
    display: flex;
    /* justify-content: flex-start; */
  }
  .title {
    font-size: 40px;
    cursor: pointer;
    font-family: 'Song Myung', serif;
    color: #196ba5;
  }

  .right {
    flex-grow: 4;
    display: flex;
    justify-content: flex-end;
    margin-right: 20px;
    margin-top: 40px;
    svg {
      cursor: pointer;
      color: #565656;
      padding: 0 15px;
    }
    button {
      width: 70px;
      height: 30px;
      font-size: 17px;
      border: none;
      color: #5f6060;
      cursor: pointer;
      background-color: white;
    }
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        display: none;
      }
    }
  }
  .autocomplete-wrapper {
    flex-grow: 0;
    margin-left: auto;
    margin-right: auto;
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        display: none;
      }
    }
  }
  .menu-bar {
    display: none;
    margin-left: auto;
    align-items: center;
    margin-top: 35px;
    font-size: 35px;
    color: #196ba5;
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        display: flex;
      }
    }
  }
`;

export default LoginHeader;
