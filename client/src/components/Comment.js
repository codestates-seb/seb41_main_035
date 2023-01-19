import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { HiOutlinePaperAirplane } from 'react-icons/hi';
import dummyData from '../db/dummyData.json';

import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
const BREAK_POINT_PC = 1300;

const Comment = () => {
  const data = dummyData.posts;
  const params = useParams();
  const url = 'http://13.125.30.88/comment';
  const [commentData, setCommentData] = useState([]);
  const [contentValue, setContentValue] = useState('');

  const onContentChange = (e) => {
    setContentValue(e.currentTarget.value);
  };
  const onPostComment = (content) => {
    if (!contentValue) {
      alert('댓글을 입력해주세요.');
      return;
    } else {
      axios(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // Authorization: `Bearer ${getLocalStorage()}`,
        },
        data: JSON.stringify({
          content,
        }),
      })
        .then((res) => {
          if (res) {
            window.location.replace('/postview/:id'); //새로고침
          }
        })
        .catch((err) => {
          return err;
        });
    }
  };
  useEffect(() => {
    const fetchData = async () => {
      try {
        // const response = await axios.get(url + `/post/` + [params.id]);
      } catch (err) {
        window.alert('오류가 발생했습니다.');
        return err;
      }
      //데이터 받아오기 가능하면 지우고 response.data로 변경
      const comment = {
        data: {
          commentId: 3,
          content: '와우',
          createdDate: '2023-01-17T17:09:00.712765',
          updatedDate: '2023-01-17T17:09:00.712765',
          nickname: '수정된 닉네임',
          profileImageUrl:
            'https://user-images.githubusercontent.com/74748851/212484014-b22c7726-1091-4b89-a9d5-c97d72b82068.png',
        },
      };
      setCommentData(comment.data);
      console.log(comment.data);
    };
    fetchData();
  }, []);
  const onDelteComment = () => {
    if (window.confirm('삭제 하시겠습니까?')) {
      axios(url + `/${commentData.commentId}`, {
        method: 'DELETE',
      })
        .then((res) => {
          if (res) {
            window.location.replace('/postview/:id'); //새로고침
          }
        })
        .catch((err) => {
          return err;
        });
    }
  };
  //{commentData로 mapping하기}

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
    height: 16vh;
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
