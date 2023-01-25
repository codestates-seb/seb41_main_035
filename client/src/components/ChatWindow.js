/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from './Avatar';
import { MdNavigateBefore } from 'react-icons/md';
import { AiOutlineClose } from 'react-icons/ai';
import { HiOutlinePaperAirplane } from 'react-icons/hi';

const ChatWindow = ({ onChatOpen, onChatWindow }) => {
  return (
    <>
      <SModalBack />
      <SWrapper>
        <AiOutlineClose className="close-container" onClick={onChatOpen} />
        <div className="chat-container">
          <div className="top">
            <div className="user-info">
              <Avatar size="45px" />
              <span className="user-name">uuuuu</span>
            </div>
            <MdNavigateBefore onClick={onChatWindow} />
          </div>
          <SContent>
            {/* 데이터 받으면 mapping하기 
          상대방과 나 데이터를 받으면 어떻게 구현할지 생각해보기*/}
            <div className="chat-content">
              <Avatar size="40px" />
              <div className="user-content">
                <div className="user-content_top">
                  <span className="user-name">yuuuuu</span>
                  <span className="user-send-time">12:00</span>
                </div>
                <div className="send-content">안녕하세요</div>
              </div>
            </div>
          </SContent>
          <SInputContent>
            <textarea rows="1" cols="33" />
            <HiOutlinePaperAirplane />
          </SInputContent>
        </div>
      </SWrapper>
    </>
  );
};
export const SModalBack = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  position: fixed;
  top: 0;
`;
const SWrapper = styled.div`
  position: fixed;
  overflow: hidden;
  width: 100%;
  top: 5%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  .close-container {
    margin-top: 30px;
    background-color: rgba(0, 0, 0, 0);
    transform: translate(700%, 10%);
    font-size: 30px;
    cursor: pointer;
  }
  .chat-container {
    margin: 10px 0px 50px 0px;
    width: 55vh;
    height: 75vh;
    background-color: #cadde5;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .top {
      display: flex;
      align-items: center;
      width: 80%;
      margin-top: 10px;
      font-size: 30px;
      justify-content: space-between;
      background-color: #cadde5;
      .user-info {
        display: flex;
        padding-right: 20px;
        background-color: #cadde5;
        .user-name {
          padding-left: 20px;
          background-color: #cadde5;
        }
        div {
          background-color: #cadde5;
        }
      }
      svg {
        cursor: pointer;
        background-color: #cadde5;
      }
    }
  }
`;
const SContent = styled.div`
  width: 80%;
  height: 50vh;
  background-color: #f4f1e0;
  margin: 10px 0px 20px 0px;
  overflow: auto;
  .chat-content {
    display: flex;
    margin: 15px 0px 5px 15px;
    font-size: 18px;
    align-items: center;
    .user-content {
      margin-left: 8px;
      .user-name {
        font-weight: bold;
        margin-right: 8px;
      }
      .user-send-time {
        font-size: 12px;
      }
    }
  }
`;
const SInputContent = styled.div`
  width: 80%;
  height: 6vh;
  background-color: #f4f1e0;
  margin-bottom: 30px;
  display: flex;
  textarea {
    border: none;
    resize: none;
    background-color: #f4f1e0;
    font-size: 20px;
    &:focus {
      outline: none;
    }
  }
  svg {
    font-size: 40px;
    margin: 8px 10px 0px 20px;
    transform: rotate(90deg);
  }
`;
export default ChatWindow;
