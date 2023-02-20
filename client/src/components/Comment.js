/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { HiOutlinePaperAirplane } from 'react-icons/hi';
import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import userStore from '../store/userStore';
const BREAK_POINT_PC = 1300;
const token = localStorage.getItem('accessToken');
const Comment = ({ profile }) => {
  const params = useParams();
  const API_URL = process.env.REACT_APP_API_URL;
  const [commentData, setCommentData] = useState([]);
  const [contentValue, setContentValue] = useState('');
  const Id = params.boardId;
  const myprofile = JSON.parse(localStorage.getItem('myprofile'));

  //추가부분
  const { nickname } = userStore((state) => state);

  const onContentChange = (e) => {
    setContentValue(e.currentTarget.value);
  };
  console.log(contentValue);
  const onPostComment = (e) => {
    e.preventDefault();
    axios(API_URL + `comment`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
      },
      data: JSON.stringify({
        boardId: Id,
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
        API_URL + `comment/board/${params.boardId}?page=1&size=10`
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
      axios(API_URL + `comment/${id}`, {
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

  //댓글 수정부분
  const [revise, setRevise] = useState(''); //댓글 수정창에 입력한 값이 저장
  const [editCommentId, setEditCommentId] = useState(''); // 현재수정중인 CommentId '' 값이면 댓글 수정중 아님
  const onChangeInput = (e) => {
    setRevise(e.target.value);
  };

  // 댓글수정 저장
  const onSave = async (id) => {
    try {
      const token = localStorage.getItem('accessToken');
      await axios.patch(
        `${API_URL}comment/${id}`,
        {
          content: revise,
        },
        {
          headers: { Authorization: token },
        },
        { withCredentials: true }
      );
    } catch (err) {
      window.alert(err);
    }
    setEditCommentId('');
    // 저장시 현재 수정중인 Comment의 Id를 ''으로 변경하여 초기화
    setCommentData((prev) => {
      return {
        data: prev.data.map((comment) => {
          if (comment.commentId === id) {
            return {
              ...comment,
              content: revise,
            };
          }
          return comment;
        }),
        pageInfoDto: prev.pageInfoDto,
      };
    });
  };

  return (
    <SWrapper>
      <div className="comment_count">댓글 {commentData.data?.length}</div>
      <div className="line"></div>
      <form className="commentWrap">
        <div className="my_avatar">
          <Avatar image={myprofile} />
        </div>
        <div className="user-name"></div>
        <div className="comment-input">
          <input
            type="text"
            placeholder="댓글달기..."
            value={contentValue}
            onChange={onContentChange}
          />

          <button disabled={!contentValue} onClick={onPostComment}>
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
                {editCommentId === comment.commentId ? ( //현재 수정중인 Comment와 동일한CommentId를 가지고있는지?
                  <SInput
                    // value={comment.content}
                    value={revise}
                    onChange={onChangeInput}
                  ></SInput>
                ) : (
                  <div className="comment_content">{comment.content}</div>
                )}
              </div>
              <div className="comment-right">
                {editCommentId === comment.commentId ? (
                  <SSave onClick={() => onSave(comment.commentId)}> 저장</SSave>
                ) : (
                  <>
                    <span
                      role="presentation"
                      //추가부분
                      onClick={() => {
                        if (comment.nickname === nickname) {
                          setRevise(comment.content);
                          setEditCommentId(comment.commentId);
                        }
                      }}
                    >
                      수정
                    </span>
                    <span
                      role="presentation"
                      //추가부분
                      onClick={() => {
                        if (comment.nickname === nickname) {
                          onDelteComment(comment.commentId);
                        }
                      }}
                    >
                      삭제
                    </span>
                  </>
                )}
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
  padding-top: 8px;

  .line {
    width: 100%;
    text-align: center;
    border-bottom: 1px solid gray;
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
      border: 1px solid lightgray;
      /* background-color: white; */
      /* background-color: #f7f5ec; */
      margin-left: 2px;
      border-radius: 5px;

      input {
        width: 90%;
        height: 4vh;
        border: none;
        /* border-bottom: 1px solid gray; */
        /* background-color: #f7f5ec; */
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
      background-color: white;
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
      height: 50px;
      align-items: center;
      justify-content: space-between;
      .comment-left {
        display: flex;
      }
      .comment-right {
        margin-right: 10px;
        font-size: 12px;
        color: gray;
        width: 80px;
        justify-content: center;
        display: flex;
        span {
          margin: 0px 5px;
          cursor: pointer;
          :hover {
            color: black;
          }
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
      margin: 3px 10px;
      font-size: 16px;
      font-weight: bold;
      span {
        :hover {
          color: black;
          cursor: pointer;
        }
      }
    }
    .comment_content {
      margin-top: 5px;
      font-size: 14px;
      width: 26vw;
      height: 100%;
    }
  }
`;
const SSave = styled.div`
  /* width: 70px;
  border-radius: 20px; */
`;
const SInput = styled.input`
  width: 22vw;
  display: flex;
  /* justify-content: center; */
  font-size: 14px;
`;

export default Comment;
