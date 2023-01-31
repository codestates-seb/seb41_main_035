/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { HiOutlinePaperAirplane } from 'react-icons/hi';
import { BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';

const Chat = () => {
  const [chatData, setChatData] = useState([]); //get
  const [sentData, setSentData] = useState([]); //post
  const [listData, setListData] = useState([]); //list get
  const [idData, setIdData] = useState('');
  const [sentName, setSentName] = useState('');
  const url = 'http://13.125.30.88';
  const sentId = JSON.parse(localStorage.getItem('sentId'));
  const name = JSON.parse(localStorage.getItem('name'));
  const myId = JSON.parse(localStorage.getItem('myId'));
  console.log(myId);
  const token = localStorage.getItem('accessToken');
  console.log(sentId);
  console.log(listData);
  console.log(chatData);
  const onChatChange = (e) => {
    setSentData(e.currentTarget.value);
  };
  useEffect(() => {
    fetchData();
    fetchListClickData();
    fetchListData();
  }, [sentId, idData]);
  useEffect(() => {
    if (chatData?.length === 0) {
      setSentName(name);
    } else if (chatData?.length !== 0 && myId === chatData[0]?.senderId) {
      setSentName(chatData[0]?.receiverNickname);
    } else if (chatData?.length !== 0 && myId !== chatData[0]?.senderId) {
      setSentName(chatData[0]?.senderNickname);
    }
  }, [chatData]);
  console.log(sentName);
  const fetchData = async () => {
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
    } catch (err) {
      return err;
    }
  };
  const fetchListClickData = async () => {
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
    } catch (err) {
      return err;
    }
  };

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

  const onPostChat = () => {
    axios(url + '/message', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
      },
      data: JSON.stringify({
        content: sentData,
        receiverNickname: sentName,
      }),
    })
      .then((res) => {
        if (res) {
          fetchData();
          fetchListData();
          fetchListClickData();
        }
      })
      .catch((err) => {
        return err;
      });
  };

  return (
    <>
      <SWrapper>
        <div className="chat">
          <div className="chatlist-container">
            <p>Chatting List</p>
            {listData.map((chat) => (
              <SChatList key={chat.messageRoom}>
                {myId === chat.senderId ? (
                  <div
                    className="chat-box"
                    role="presentation"
                    onClick={() => setIdData(chat.receiverId)}
                  >
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
                ) : (
                  <div
                    className="chat-box"
                    role="presentation"
                    onClick={() => setIdData(chat.senderId)}
                  >
                    <Avatar size="45px" image={chat.senderProfileImageUrl} />
                    <div className="right content">
                      <div className="user-name">{chat.senderNickname}</div>
                      <div className="content_and_time">
                        <div className="chat-last_content">{chat.content}</div>
                        <div className="chat-time">
                          {new Date(chat.createdDate).toLocaleString()}
                        </div>
                      </div>
                    </div>
                  </div>
                )}
              </SChatList>
            ))}
          </div>
          <div className="chat-container">
            <div className="top">
              {myId === chatData[0]?.senderId ? (
                <Avatar
                  size="40px"
                  image={chatData[0]?.receiverProfileImageUrl}
                />
              ) : (
                <Avatar
                  size="40px"
                  image={chatData[0]?.senderProfileImageUrl}
                />
              )}
              <span className="user-name">{sentName}</span>
            </div>

            <SContent>
              {chatData.map((chat) => (
                <div className="chat-content" key={chat.messageId}>
                  <Avatar size="40px" image={chat.senderProfileImageUrl} />
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
  margin-top: 20px;

  .chat {
    display: flex;
    margin-left: 200px;
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      & {
        margin-left: 0px;
      }
    }
  }
  p {
    margin-left: 40px;
    font-size: 25px;
  }
  .chatlist-container {
    margin: 10px 0px 50px 0px;
    width: 330px;
    height: 75vh;
    /* background-color: #e1f2f9; */
    border-radius: 10px 0px 0px 10px;
    display: flex;
    flex-direction: column;
    border: 1px solid gray;
    overflow: auto;
    &::-webkit-scrollbar {
      width: 7px;
      background-color: white;
    }
    &::-webkit-scrollbar-thumb {
      background: lightgray;
      border-radius: 5px;
    }
  }
  .chat-container {
    margin: 10px 0px 50px 0px;
    /* padding-left: 10px; */
    width: 400px;
    height: 75vh;
    /* background-color: #e1f2f9; */
    border-radius: 0px 10px 10px 0px;
    border: 1px solid gray;
    display: flex;
    flex-direction: column;
    justify-content: center;
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      & {
        width: 350px;
      }
    }
    .top {
      display: flex;
      align-items: center;
      /* width: 105%; */
      font-size: 26px;
      padding-bottom: 13px;
      padding-top: 10px;
      justify-content: flex-start; //추가
      padding-left: 15px;
      border-bottom: 1px solid lightgray;
      .user-name {
        padding-left: 20px;
      }
    }
  }
`;
const SChatList = styled.div`
  display: flex;
  justify-content: center;
  border-top: 1px solid lightgray;
  border-bottom: 1px solid lightgray;
  &:focus {
    background-color: gray;
  }
  .chat-box {
    cursor: pointer;
    width: 85%;
    height: 8.5vh;
    background-color: white;
    display: flex;
    align-items: center;
    justify-content: felx-start;
    padding-left: 10px;
    /* margin-bottom: 10px; */
    border-radius: 10px;
    /* box-shadow: 3px 3px 5px gray; */

    .content {
      margin-left: 10px;
      flex-grow: 1;
    }
    .content_and_time {
      margin-top: 5px;
      width: 90%;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .chat-time {
      font-size: 10px;
    }
  }
`;

const SContent = styled.div`
  width: 100%;
  height: 55vh;
  background-color: white;
  margin: 10px 0px 20px 0px;
  overflow: auto;
  border-radius: 8px; //추가
  .chat-content {
    display: flex;
    margin: 15px 0px 5px 15px;
    font-size: 18px;
    align-items: center;
    .user-content {
      padding: 5px;
      margin-left: 10px;
      width: 300px;
      background-color: #e1f2f9;
      border-radius: 10px 10px 10px 10px;
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
  width: 90%;
  height: 5vh;
  background-color: white;
  /* margin-bottom: 30px; */
  margin-left: 15px;
  display: flex;
  border: 1px solid gray;
  border-radius: 8px; //추가
  textarea {
    border: none;
    resize: none;
    background-color: white;
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
    background-color: white;
    border: none;
    border-radius: 8px;
  }
`;
export default Chat;
