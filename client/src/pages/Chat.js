/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { HiOutlinePaperAirplane } from 'react-icons/hi';
import { token } from '../constants/index';
import userStore from '../store/userStore';
const Chat = () => {
  const [chatData, setChatData] = useState([]); //get
  const [sentData, setSentData] = useState([]); //post
  const [listData, setListData] = useState([]); //list get
  const [idData, setIdData] = useState('');
  const url = 'http://13.125.30.88';
  const sentId = JSON.parse(localStorage.getItem('sentId'));
  const name = JSON.parse(localStorage.getItem('name'));

  const onChatChange = (e) => {
    setSentData(e.currentTarget.value);
  };
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
      setChatData(response.data.data);
      console.log(response.data.data);
    } catch (err) {
      return err;
    }
  };
  const fetchListClickData = async () => {
    try {
      const response = await axios.get(
        url + `/message/received/${idData}?page=1&size=100`,
        {
          headers: {
            Authorization: token,
          },
        }
      );
      setChatData(response.data.data);
      console.log(response.data.data);
    } catch (err) {
      return err;
    }
  };
  useEffect(() => {
    fetchData();
    fetchListClickData();
  }, [sentId, idData]);
  //목록조회
  const fetchListData = async () => {
    try {
      const response = await axios.get(url + `/message/room`, {
        headers: {
          Authorization: token,
        },
      });
      setListData(response.data);
    } catch (err) {
      return err;
    }
  };
  useEffect(() => {
    fetchListData();
  }, []);
  console.log(listData);
  console.log(chatData);
  const onPostChat = () => {
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
          fetchData();
          fetchListData(); //새로고침
        }
      })
      .catch((err) => {
        return err;
      });
  };
  return (
    <>
      <SWrapper>
        <div className="chatlist-container">
          <p>채팅</p>
          {listData.map((chat) => (
            <SChatList
              key={chat.messageRoom}
              onClick={() => setIdData(chat.receiverId)}
            >
              <div className="chat-box">
                <Avatar size="45px" image={chat.receiverProfileImageUrl} />
                <div className="right content">
                  <div className="user-name">{chat.receiverNickname}</div>
                  <div className="content_and_time">
                    <div className="chat-last_content">{chat.content}</div>
                    <div className="chat-time">
                      {new Date(chat.createdDate).toLocaleString()}
                    </div>
                  </div>
                </div>
              </div>
            </SChatList>
          ))}
        </div>
        <div className="chat-container">
          <div className="top">
            <div className="user-info">
              <Avatar size="45px" />
              <span className="user-name">{name}</span>
            </div>
          </div>
          <SContent>
            {chatData.map((chat) => (
              <div className="chat-content" key={chat.messageId}>
                <Avatar size="40px" />
                <div className="user-content">
                  <div className="user-content_top">
                    <span className="user-name">{chat.senderNickname}</span>
                    <span className="user-send-time">
                      {new Date(chat.createdDate).toLocaleString()}
                    </span>
                  </div>
                  <div className="send-content">{chat.content}</div>
                </div>
              </div>
            ))}
          </SContent>
          <SInputContent>
            <textarea rows="1" cols="33" onChange={onChatChange} />
            <button disabled={!sentData} onClick={onPostChat}>
              <HiOutlinePaperAirplane />
            </button>
          </SInputContent>
        </div>
      </SWrapper>
    </>
  );
};

const SWrapper = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  margin-top: 20px;
  p {
    margin-left: 40px;
    font-size: 25px;
    background-color: #cadde5;
  }
  .chatlist-container {
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
const SIIWrapper = styled.div`
  /* position: fixed; */
  width: 100%;
  //추가 height 100%
  height: 100%;
  top: 5%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  /* z-index: 99999; //추가 */
  margin-top: 20px;
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
    margin: 3px 10px 0px 20px; //추가
    transform: rotate(90deg);
  }
  button {
    background-color: #f4f1e0;
    border: none;
  }
`;
export default Chat;
