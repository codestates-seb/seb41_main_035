import styled from 'styled-components';
import dummyData from '../db/dummyData.json';
import Avatar from '../components/Avatar';
import { HiOutlinePaperAirplane } from 'react-icons/hi';

const Comment = () => {
  const data = dummyData.posts;
  return (
    <SWrapper>
      <div className="comment_count">댓글 {data.length}</div>
      <div className="line"></div>
      <div className="comment_container">
        {data.map((comment) => (
          <div className="comment_box" key={comment.id}>
            <div className="user_avatar">
              {' '}
              <Avatar image={comment.avatar} />
            </div>

            <div className="user_name">{comment.userNickname}</div>
            <div className="comment_content">{comment.content}</div>
          </div>
        ))}
      </div>
      <form className="commentWrap">
        <div className="my_avatar">
          <Avatar />
        </div>
        <div className="comment-input">
          <input type="text" placeholder="댓글달기..." />
          <HiOutlinePaperAirplane />
        </div>
      </form>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  width: 100%;
  .line {
    width: 100%;
    text-align: center;
    border-bottom: 1px solid #aaa;
    line-height: 0.1em;
    margin: 10px 0;
  }

  .commentWrap {
    margin-top: 10px;
    display: flex;
    .my_avatar {
      width: 2vw;
      height: 30px;
      object-fit: cover;
      position: relative;
      overflow: hidden;
      margin-right: 10px;

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 100%;
        height: 100%;
      }
    }
    .comment-input {
      display: flex;
      width: 90%;
      border: 1px solid gray;
      input {
        width: 90%;
        height: 4vh;
        border: none;
        &:focus {
          outline: none;
        }
      }
      svg {
        font-size: 25px;
        transform: rotate(90deg);
        margin-top: 5px;
      }
    }
  }
  .comment_container {
    height: 95px;
    overflow: auto;
    .comment_box {
      display: flex;
      height: 40px;
      align-items: center;
    }
    .user_avatar {
      width: 2vw;
      height: 30px;
      object-fit: cover;
      position: relative;
      overflow: hidden;

      img {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 100%;
        height: 100%;
      }
    }
    .user_name {
      margin: 0px 10px;
      font-size: 18px;
      font-weight: bold;
    }
  }
`;
const commetBtn = styled.div`
  background-color: pink;
`;
export default Comment;
