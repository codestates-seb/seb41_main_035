/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from './Avatar';
import { MdNavigateBefore } from 'react-icons/md';
import { AiOutlineClose } from 'react-icons/ai';
import { HiOutlinePaperAirplane } from 'react-icons/hi';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { token } from '../constants/index';
const ChatWindow = ({ onChatOpen, onChatWindow, sentId, name }) => {
  const [chatData, setChatData] = useState([]);
  const [sentData, setSentData] = useState([]);
  const url = 'http://13.125.30.88';
  const onChatChange = (e) => {
    setSentData(e.currentTarget.value);
    console.log(sentData);
  };
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          url + `/message/received/${sentId}?page=1&size=100`,
          {
            headers: {
              Authorization: token,
            },
          }
        );
        setChatData(response.data);
        console.log(response.data.data);
      } catch (err) {
        return err;
      }
    };
    fetchData();
  }, []);
  console.log(chatData);
  const onPostComment = () => {
    if (!sentData) {
      alert('글을 입력해주세요.');
      return;
    } else {
      axios(url + '/message', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: token,
        },
        data: JSON.stringify({
          content: sentData,
          receiverNickname: name,
        }),
      })
        .then((res) => {
          if (res) {
            window.location.replace('/'); //새로고침
          }
        })
        .catch((err) => {
          return err;
        });
    }
  };
  return (
    <>
      <SModalBack />
      <SWrapper>
        <AiOutlineClose className="close-container" onClick={onChatOpen} />
        <div className="chat-container">
          <div className="top">
            {/* 뒤로가기 왼쪽으로 이동 */}
            <MdNavigateBefore onClick={onChatWindow} />
            <div className="user-info">
              <Avatar size="45px" />
              <span className="user-name">uuuuu</span>
            </div>
            {/* <MdNavigateBefore onClick={onChatWindow} /> */}
          </div>
          <SContent>
            {/* 데이터 받으면 mapping하기 
          상대방과 나 데이터를 받으면 어떻게 구현할지 생각해보기*/}
            {/* {chatData.map((chat) => ( */}
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
            {/* ))} */}
          </SContent>
          <SInputContent>
            <textarea rows="1" cols="33" onChange={onChatChange} />
            <HiOutlinePaperAirplane onClick={onPostComment} />
          </SInputContent>
        </div>
      </SWrapper>
    </>
  );
};
export const SModalBack = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  position: fixed;
  top: 0;
  z-index: 999; //로그인헤더만 하얘서 추가
`;
const SWrapper = styled.div`
  position: fixed;
  width: 100%;
  //추가 height 100%
  height: 100%;
  top: 5%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  z-index: 99999; //추가
  .close-container {
    /* margin-top: 30px; */
    background-color: rgba(0, 0, 0, 0);
    transform: translate(700%, 10%);
    font-size: 30px;
    cursor: pointer;
    margin-left: 50px; //추가
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
      width: 90%;
      margin-top: 10px;
      font-size: 30px;
      /* justify-content: space-between; */
      background-color: #cadde5;
      justify-content: flex-start; //추가

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
        //추가
        margin: -5px 20px 0px -20px;
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
  border-radius: 8px; //추가
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
  border-radius: 8px; //추가
  textarea {
    border: none;
    resize: none;
    background-color: #f4f1e0;
    font-size: 20px;
    border-radius: 8px; //추가

    &:focus {
      outline: none;
    }
  }
  svg {
    font-size: 30px; //추가
    margin: 13px 10px 0px 20px; //추가
    transform: rotate(90deg);
  }
`;
export default ChatWindow;
