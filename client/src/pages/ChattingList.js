import styled from 'styled-components';
import dummyData from '../db/dummyData.json';
import Avatar from '../components/Avatar';
import { useNavigate } from 'react-router-dom';
const ChattingList = () => {
  const navigate = useNavigate();
  const data = dummyData.posts;
  return (
    <SWrapper>
      <div className="chat-container">
        <p>채팅내역</p>
        {data.map((chat) => (
          <SChatList key={chat.id}>
            <div
              className="chat-box"
              role="presentation"
              onClick={() => navigate(`/chat`)}
            >
              <Avatar />
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
  );
};
const SWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  p {
    margin-left: 40px;
    font-size: 25px;
  }
  .chat-container {
    margin: 50px;
    width: 55vh;
    height: 80vh;
    background-color: #d9d9d9;
    display: flex;
    flex-direction: column;
    overflow: auto;
  }
`;
const SChatList = styled.div`
  display: flex;
  justify-content: center;
  .chat-box {
    cursor: pointer;
    width: 85%;
    height: 10vh;
    background-color: skyblue;
    display: flex;
    align-items: center;
    padding-left: 10px;
    margin-bottom: 10px;
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
