/* eslint-disable react/prop-types */
import styled from 'styled-components';
import dummyData from '../db/dummyData.json';
import Avatar from './Avatar';
import { useState, useEffect } from 'react';
import { AiOutlineClose } from 'react-icons/ai';
import ChatWindow from './ChatWindow';
import axios from 'axios';
const ChattingList = ({ onChatOpen }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [chatData, setChatDat] = useState([]);
  const onChatWindow = () => {
    setIsOpen((prev) => !prev);
  };
  const data = dummyData.posts;
  return (
    <>
      <SModalBack />
      <SWrapper>
        <AiOutlineClose className="close-container" onClick={onChatOpen} />
        <div className="chat-container">
          <p>채팅</p>
          {data.map((chat) => (
            <SChatList key={chat.id}>
              <div
                className="chat-box"
                role="presentation"
                onClick={onChatWindow}
              >
                <Avatar size="45px" image={chat.avatar} />
                <div className="right content">
                  <div className="user-name">{chat.userNickname}</div>
                  <div className="content_and_time">
                    <div className="chat-last_content">hiiiiiii</div>
                    <div className="chat-time">1월 12일 (목) 12:00</div>
                  </div>
                </div>
              </div>
            </SChatList>
          ))}
        </div>
      </SWrapper>
      {isOpen && (
        <ChatWindow onChatWindow={onChatWindow} onChatOpen={onChatOpen} />
      )}
    </>
  );
};
export const SModalBack = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  position: fixed;
  top: 0;
`;
const SWrapper = styled.div`
  position: fixed;
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
    font-size: 30px;
    cursor: pointer;
    transform: translate(700%, 10%);
  }
  p {
    margin-left: 40px;
    font-size: 25px;
    background-color: #cadde5;
  }
  .chat-container {
    margin: 10px 0px 50px 0px;
    width: 55vh;
    height: 75vh;
    background-color: #cadde5;
    display: flex;
    flex-direction: column;
    overflow: auto;
    &::-webkit-scrollbar {
      width: 7px;
      background-color: #cadde5;
    }
    &::-webkit-scrollbar-thumb {
      background: white;
      border-radius: 5px;
    }
  }
`;
const SChatList = styled.div`
  display: flex;
  justify-content: center;
  background-color: #cadde5;
  .chat-box {
    cursor: pointer;
    width: 85%;
    height: 9.5vh;
    background-color: #f4f1e0;
    display: flex;
    align-items: center;
    padding-left: 10px;
    margin-bottom: 10px;
    border-radius: 10px;
    box-shadow: 3px 3px 5px gray;
    .content {
      margin-left: 10px;
      flex-grow: 1;
    }
    .content_and_time {
      margin-top: 14px;
      width: 90%;
      display: flex;
      justify-content: space-between;
    }
  }
`;

export default ChattingList;
