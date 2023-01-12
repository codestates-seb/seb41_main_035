import styled from 'styled-components';
import dummyData from '../db/dummyData.json';
import Avatar from '../components/Avatar';
import { MdNavigateBefore } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
const ChatWindow = () => {
  const navigate = useNavigate();

  return (
    <SWrapper>
      <div className="chat-container">
        <div className="top">
          <div className="user-info">
            <Avatar />
            <span className="user-name">yuuuuu</span>
          </div>
          <MdNavigateBefore onClick={() => navigate(`/chattinglist`)} />
        </div>
      </div>
    </SWrapper>
  );
};
const SWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  .chat-container {
    margin: 50px;
    width: 55vh;
    height: 80vh;
    background-color: #d9d9d9;
    display: flex;
    flex-direction: column;
    overflow: auto;
    .top {
      display: flex;
      align-items: center;
      margin: 20px;
      font-size: 30px;
      justify-content: space-between;
      .user-info {
        display: flex;
        padding-right: 20px;
      }
      .user-name {
        padding-left: 20px;
      }
      svg {
        cursor: pointer;
      }
    }
  }
`;
export default ChatWindow;
