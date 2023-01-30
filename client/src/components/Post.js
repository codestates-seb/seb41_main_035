/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from './Avatar';
import { BsBookmarkHeart, BsBookmarkHeartFill } from 'react-icons/bs';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { BREAK_POINT_TABLET, token } from '../constants/index';
import axios from 'axios';
const backendUrl = 'http://13.125.30.88/';
// 게시물 하나에 해당하는 컴포넌트 -게시물 하나에 대한 정보를 나타냄
const Post = ({ post }) => {
  const navigate = useNavigate();
  // 게시물좋아요 카운트
  const [likeCnt, setLikeCnt] = useState(post.likeCnt);
  // 게시물좋아요 여부 ture, false
  const [isLike, setIsLike] = useState(post.like);
  const onClickGood = async (id) => {
    const token = localStorage.getItem('accessToken');
    const res = await axios.post(
      `${backendUrl}boards/${id}/like`, // 좋아요 API
      {},
      {
        headers: { Authorization: token },
      }
    );
    if (res && res?.data) {
      setLikeCnt(res.data.likeCnt);
      setIsLike((prev) => !prev);
    }
  };

  return (
    <PostBoxOne key={post.boardId}>
      <div className="user-info ">
        <div
          className="user-info left"
          role="presentation"
          onClick={() => {
            navigate(`/profile/${post.member?.memberId}`);
          }}
        >
          <Avatar size="25px" image={post.member?.profileImageUrl} />
          <div className="user-info name">{post.member?.nickname}</div>
        </div>
        <div className="user-info right">
          <span className="user-info height">{post.member?.height}cm</span>
          <span className="user-info weight">{post.member?.weight}kg</span>
        </div>
      </div>

      <div
        className="style-picture"
        role="presentation"
        onClick={() => {
          navigate(`/postview/${post.boardId}`);
        }}
      >
        <Avatar size="400px" image={post.userImage} />
      </div>
      <div
        className="container add"
        role="presentation"
        onClick={() => onClickGood(post.boardId)}
      >
        {isLike ? <BsBookmarkHeartFill /> : <BsBookmarkHeart />}
      </div>
    </PostBoxOne>
  );
};

const PostBoxOne = styled.div`
  height: 390px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0px 15px 60px 15px;
  border: 2px solid #949494;
  border-radius: 5px;
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    & {
      height: 60vh;
    }
  }
  .user-info {
    display: flex;
    width: 90%;
    padding-top: 6px;
    align-items: center;
    /* justify-content: flex-start; */
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        width: 85%;
      }
    }
    .left {
      display: flex;
      width: 300%;
      .name {
        margin-left: 10px;
        margin-bottom: 7px;
        font-size: 20px;
        cursor: pointer;
      }
    }
    .right {
      color: #2e2d2a;
      font-size: 13px;
      flex-grow: 1;
    }
  }
  .style-picture {
    cursor: pointer;
    width: 258px;
    height: 300px;
    object-fit: cover;
    position: relative;
    overflow: hidden;
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        width: 55vw;
        height: 400px;
      }
    }
    img {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 100%;
      height: 100%;
    }
  }
  .add {
    display: flex;
    width: 90%;
    justify-content: flex-end;
    margin: 8px 0px;
    svg {
      font-size: 20px;
      cursor: pointer;
      @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
        & {
          font-size: 30px;
        }
      }
    }
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        width: 85%;
      }
    }
  }
`;

export default Post;
