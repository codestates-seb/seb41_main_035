/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { HiOutlinePaperAirplane } from 'react-icons/hi';
import { token, BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';

const Chat = () => {
  const [chatData, setChatData] = useState([]); //get
  const [sentData, setSentData] = useState([]); //post
  const [listData, setListData] = useState([]); //list get
  const [idData, setIdData] = useState('');
  const url = 'http://13.125.30.88';
  const sentId = JSON.parse(localStorage.getItem('sentId'));
  const name = JSON.parse(localStorage.getItem('name'));
  const myId = JSON.parse(localStorage.getItem('myId'));
  console.log(myId);
  const sentname = chatData[0]?.receiverNickname;
  const onChatChange = (e) => {
    setSentData(e.currentTarget.value);
  };
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
  useEffect(() => {
    fetchData();
    fetchListData();
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
        receiverNickname: sentname,
      }),
    })
      .then((res) => {
        if (res) {
          fetchData();
          fetchListData();
          fetchListClickData(); //새로고침
        }
      })
      .catch((err) => {
        return err;
      });
  };
  const onDelteMyChat = (id) => {
    if (window.confirm('삭제 하시겠습니까?')) {
      axios(url + `//message/received/${id}`, {
        method: 'delete',
        headers: {
          Authorization: token,
        },
      })
        .then((res) => {
          if (res) {
            fetchData();
            fetchListData();
            fetchListClickData();
          }
        })
        .catch((err) => console.log('Error', err.message));
    }
  };
  return (
    <>
      <SWrapper>
        <div className="chat">
          <div className="chatlist-container">
            <p>Chatting List</p>
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
              <Avatar
                size="40px"
                image={chatData[0]?.receiverProfileImageUrl}
              />
              <span className="user-name">{chatData[0]?.receiverNickname}</span>
            </div>
            <SContent>
              {chatData.map((chat) => (
                <div className="chat-content" key={chat.messageId}>
                  <Avatar size="40px" image={chat.senderProfileImageUrl} />
                  <div
                    className="user-content"
                    role="presentation"
                    onClick={() => {
                      onDelteMyChat(chat.messageId);
                    }}
                  >
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
  /* flex-direction: row; */
  margin-top: 20px;

  .chat {
    display: flex;
    margin-left: 20px;
  }
  p {
    margin-left: 40px;
    font-size: 25px;
  }
  .chatlist-container {
    margin: 10px 0px 50px 0px;
    width: 330px;
    height: 75vh;
    background-color: #e1f2f9;
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
    padding-left: 10px;
    width: 480px;
    height: 75vh;
    background-color: #e1f2f9;
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
      width: 80%;
      font-size: 30px;
      margin-top: -5px;
      justify-content: flex-start; //추가
      .user-name {
        padding-left: 20px;
      }
    }
  }
`;
const SChatList = styled.div`
  display: flex;
  justify-content: center;
  .chat-box {
    cursor: pointer;
    width: 85%;
    height: 9.5vh;
    background-color: white;
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
  width: 90%;
  height: 50vh;
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
  width: 90%;
  height: 6vh;
  background-color: white;
  margin-bottom: 30px;
  display: flex;
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
