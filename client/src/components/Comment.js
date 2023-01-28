/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { HiOutlinePaperAirplane } from 'react-icons/hi';
import { BsThreeDotsVertical } from 'react-icons/bs';
import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { fetchDelete } from '../utils/CommentApi';
const BREAK_POINT_PC = 1300;
const token = localStorage.getItem('accessToken');

const Comment = ({ name, boardId }) => {
  const params = useParams();
  const url = 'http://13.125.30.88/comment';

  const [commentData, setCommentData] = useState([]);
  const [contentValue, setContentValue] = useState('');
  const [removeData, setRemoveData] = useState(commentData.data);
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
          location.href = '/postview/' + [params.boardId]; //새로고침
        }
      })
      .catch((err) => {
        return err;
      });
  };
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          url + `/board/${params.boardId}?page=1&size=10`
        );
        setCommentData(response.data.data);
        console.log(response.data);
      } catch (err) {
        return err;
      }
      //데이터 받아오기 가능하면 지우고 response.data로 변경
    };
    fetchData();
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
          if (res.ok) {
            setRemoveData({ commentId: 0 });
          }
        })
        .catch((err) => console.log('Error', err.message));
    }
  };

  //추가 : 댓글 수정부분
  const [revise, setRevise] = useState(''); //댓글수정창에 입력한 값이 저장되는 State
  // const [isFixing, setIsFixing] = useState(false);
  const [editCommentId, setEditCommentId] = useState(''); // 현재 수정중인 Comment의 Id. '' 값이면 댓글 수정중이 아닌 것을 의미
  const onChangeInput = (e) => {
    setRevise(e.target.value);
  };
  // const fixMode = () => {
  //   setIsFixing(true);
  // };

  // 댓글 수정한거 저장
  const onSave = async (id) => {
    try {
      const token = localStorage.getItem('accessToken');
      await axios.patch(
        `${url}/${id}`,
        // url + `/${id}`,
        {
          // boardId: boardId,
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
    // setIsFixing(false);
    setEditCommentId(''); // 저장시 현재 수정중인 Comment의 Id를 ''으로 변경하여 초기화
    setCommentData((prev) =>
      prev.map((comment) => {
        if (comment.commentId === id) {
          return {
            ...comment,
            content: revise,
          };
        }
        return comment;
      })
    );
  };

  return (
    <SWrapper>
      <div className="comment_count">댓글 {commentData.length}</div>
      <div className="line"></div>
      <form className="commentWrap">
        <div className="my_avatar">
          <Avatar />
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
        {commentData.map((comment) => (
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
                    onClick={() => {
                      setRevise(comment.content);
                      setEditCommentId(comment.commentId);
                    }}
                  >
                    수정
                  </span>
                  <span
                    role="presentation"
                    onClick={() => onDelteComment(comment.commentId)}
                  >
                    삭제
                  </span>
                </>
              )}
              {/* <span
                role="presentation"
                onClick={() => {
                  setRevise(comment.content);
                  setEditCommentId(comment.commentId);
                }}
              >
                수정
              </span>
              <span
                role="presentation"
                onClick={() => onDelteComment(comment.commentId)}
              >
                삭제
              </span>
              {editCommentId === comment.commentId ? (
                <SSave onClick={onSave}>저장</SSave>
              ) : (
                ''
              )} */}
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
  padding-top: 8px;

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
      margin-left: 2px;
      border-radius: 5px;

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
        width: 70px;
        justify-content: center;
        display: flex;
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
      margin: 3px 10px;
      font-size: 16px;
      font-weight: bold;
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
