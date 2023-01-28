/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { HiOutlinePaperAirplane } from 'react-icons/hi';
import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { token } from '../constants/index';
const BREAK_POINT_PC = 1300;

const Comment = ({ boardId, profile }) => {
  const params = useParams();
  const url = 'http://13.125.30.88/comment';
  const [commentData, setCommentData] = useState([]);
  const [contentValue, setContentValue] = useState('');
  const onContentChange = (e) => {
    setContentValue(e.currentTarget.value);
  };
  const onPostComment = () => {
    axios(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
      },
      data: JSON.stringify({
        boardId: boardId,
        content: contentValue,
      }),
    })
      .then((res) => {
        if (res) {
          fetchCommentData();
        }
      })
      .catch((err) => {
        return err;
      });
  };
  const fetchCommentData = async () => {
    try {
      const response = await axios.get(
        url + `/board/${params.boardId}?page=1&size=10`
      );
      setCommentData(response.data);
      console.log(response.data);
    } catch (err) {
      return err;
    }
    //데이터 받아오기 가능하면 지우고 response.data로 변경
  };
  useEffect(() => {
    fetchCommentData();
  }, []);

  const onDelteComment = (id) => {
    if (window.confirm('삭제 하시겠습니까?')) {
      axios(url + `/${id}`, {
        method: 'delete',
        headers: {
          Authorization: token,
        },
      })
        .then((res) => {
          if (res) {
            fetchCommentData();
          }
        })
        .catch((err) => console.log('Error', err.message));
    }
  };

  return (
    <SWrapper>
      <div className="comment_count">댓글 {commentData.data?.length}</div>
      <div className="line"></div>
      <form className="commentWrap">
        <div className="my_avatar">
          <Avatar image={profile} />
        </div>
        <div className="user-name"></div>
        <div className="comment-input">
          <input
            type="text"
            placeholder="댓글달기..."
            value={contentValue}
            onChange={onContentChange}
          />
          <button
            disabled={!contentValue}
            onClick={() => {
              onPostComment(contentValue);
            }}
          >
            <HiOutlinePaperAirplane />
          </button>
        </div>
      </form>

      <div className="comment_container">
        {commentData &&
          commentData.data?.map((comment) => (
            <div className="comment_box" key={comment.commentId}>
              <div className="comment-left">
                <div className="user_avatar">
                  <Avatar image={comment.profileImageUrl} />
                </div>
                <div className="user_name">{comment.nickname}</div>
                <div className="comment_content">{comment.content}</div>
              </div>
              <div className="comment-right">
                <span>수정</span>
                <span
                  role="presentation"
                  onClick={() => onDelteComment(comment.commentId)}
                >
                  삭제
                </span>
                {/* <BsThreeDotsVertical /> */}
              </div>
            </div>
          ))}
      </div>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  width: 100%;
  height: 50%;
  .line {
    width: 100%;
    text-align: center;
    border-bottom: 1px solid #aaa;
    line-height: 0.1em;
    margin: 10px 0;
  }
  .commentWrap {
    display: flex;
    margin-bottom: 10px;
    justify-content: center;
    align-items: center;
    .my_avatar {
      width: 30px;
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
      width: 100%;
      justify-content: center;
      /* border-bottom: 1px solid gray; */
      /* background-color: white; */
      background-color: #f7f5ec;
      margin-left: 10px;

      input {
        width: 90%;
        height: 4vh;
        border: none;
        /* border-bottom: 1px solid gray; */
        background-color: #f7f5ec;
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
    button {
      background-color: #f7f5ec;
      border: none;
      cursor: pointer;
    }
  }
  .comment_container {
    height: 12vh;

    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      & {
        height: 85px;
      }
    }
    overflow: auto;
    .comment_box {
      display: flex;
      height: 40px;
      align-items: center;
      justify-content: space-between;
      .comment-left {
        display: flex;
      }
      .comment-right {
        margin-right: 10px;
        font-size: 12px;
        color: gray;
        span {
          margin: 0px 5px;
        }
      }
    }
    .user_avatar {
      width: 30px;
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
    span {
      :hover {
        color: black;
        cursor: pointer;
      }
    }
  }
`;

export default Comment;
