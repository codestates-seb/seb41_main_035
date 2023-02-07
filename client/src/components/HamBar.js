/* eslint-disable react/prop-types */
import { AiOutlineClose, AiOutlineMessage } from 'react-icons/ai';
import { BsPersonCircle, BsPencilSquare } from 'react-icons/bs';
import styled from 'styled-components';
import SearchBox from './SearchBox';
import { useNavigate } from 'react-router-dom';
const Items = ['아우터', '상의', '하의', '원피스', '모자', '신발'];
const links = ['outer', 'top', 'bottom', 'onepiece', 'hat', 'shoes'];
const Hambar = ({ onBarOpen, userId, Logout, onClickButton }) => {
  const navigate = useNavigate();
  return (
    <>
      <SModalBack />
      <SWrapper>
        <div className="bar-container">
          <div className="bar-top">
            <div className="name">Look At Me</div>
            <AiOutlineClose className="close" onClick={onBarOpen} />
          </div>
          {!localStorage.getItem('accessToken') ? (
            <div className="bar-header">
              <button
                className="login button"
                onClick={() => {
                  onClickButton();
                  onBarOpen();
                }}
              >
                로그인 하러가기
              </button>
            </div>
          ) : (
            <div className="bar-header">
              <div className="my-profil">
                <BsPersonCircle
                  onClick={() => {
                    navigate(`/profile/${userId}`);
                    onBarOpen();
                  }}
                  size="40"
                />
                <div className="my-nickname">my profil</div>
              </div>
              <div className="logout-container">
                <button
                  className="logout button"
                  onClick={() => {
                    Logout();
                    onBarOpen();
                  }}
                >
                  Log out
                </button>
              </div>

              <div className="menu">
                <BsPencilSquare
                  size="30"
                  role="presentation"
                  onClick={() => {
                    navigate(`/postupload`);
                    onBarOpen();
                  }}
                />
                <div className="middle-line"></div>
                <AiOutlineMessage
                  size="30"
                  onClick={() => {
                    navigate(`/chatting`);
                    onBarOpen();
                  }}
                />
              </div>
            </div>
          )}
          <SearchBox />
          <div>
            <p className="title">카테고리</p>
          </div>
          <ItemList>
            {Items.map((item, index) => (
              <div
                key={index}
                className="items"
                role="presentation"
                onClick={() => {
                  navigate(`/category/${links[index]}`);
                  onBarOpen();
                }}
              >
                {item}
              </div>
            ))}
          </ItemList>
        </div>
      </SWrapper>
    </>
  );
};
export const SModalBack = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  position: fixed;
  top: 12%;
`;
const SWrapper = styled.div`
  position: fixed;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: flex-end;
  top: 12%;
  margin-top: 3px;
  z-index: 999;
  .bar-container {
    width: 200px;
    height: 100%auto;
    background-color: white;
    padding: 10px 10px 0px 10px;
  }
  .name {
    color: #196ba5;
  }
  svg {
    cursor: pointer;
    :hover {
      color: gray;
    }
  }
  button {
    cursor: pointer;
  }
  .bar-top {
    display: flex;
    width: 100%;
    justify-content: space-between;
    font-size: 20px;
  }
  .close {
    color: gray;
    font-size: 25px;
  }
  .login {
    background-color: white;
    border: none;
    border-bottom: 1px solid gray;
    font-size: 15px;
    margin-top: 30px;
  }
  .my-profil {
    display: flex;
    margin-top: 20px;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 70px;
    .my-nickname {
      margin-left: 10px;
    }
  }
  .logout-container {
    width: 100%;
    text-align: right;
    button {
      background-color: white;
      border: none;
      color: gray;
    }
  }

  .menu {
    display: flex;
    width: 100%;
    justify-content: space-around;
    border-top: 1px solid lightgray;
    border-bottom: 1px solid lightgray;
    margin-bottom: -20px;
    margin-top: 5px;

    .middle-line {
      border-left: 1px solid lightgray;
    }
    svg {
      padding: 10px 0px;
      margin: 0px 30px;
    }
  }
`;
const ItemList = styled.div`
  display: grid;
  grid-template-columns: 95px 95px;
  grid-template-rows: 1fr 1fr 1fr;

  .items {
    height: 20px;
    display: flex;
    font-size: 15px;
    cursor: pointer;
    padding: 8px 3px;

    :hover {
      color: #196ba5;
      background-color: #f4f1e0;
    }
  }
`;

export default Hambar;
