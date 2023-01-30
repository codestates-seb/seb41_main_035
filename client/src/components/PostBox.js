/* eslint-disable react/prop-types */
import styled from 'styled-components';
import Avatar from '../components/Avatar';
import { BsBookmarkHeart, BsBookmarkHeartFill } from 'react-icons/bs';
import { useNavigate } from 'react-router-dom';
import { useState, useParams } from 'react';
import { BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';
import axios from 'axios';

const backendUrl = 'http://13.125.30.88/';

// Home.js의 PostBox의 data
const PostBox = ({ data }) => {
  const navigate = useNavigate();
  // console.log(data);
  // const [like, setLike] = useState(0);
  // const onClickGood = () => {
  //   setLike(like + 1);
  // };

  // like State 를 number[] 형태의 배열로 - 값을 0 으로 채워주고
  // const [like, setLike] = useState(new Array(data?.length).fill(0));
  const [like, setLike] = useState(data.map((item) => item.likeCnt));

  const [isLike, setIsLike] = useState(data.like);
  console.log(isLike);

  const onClickGood = async (id, index) => {
    const token = localStorage.getItem('accessToken');
    const res = await axios.post(
      // `${backendUrl}boards/likes/${id}`, // 좋아요 API 를 요청
      `${backendUrl}boards/${id}/like`, // 좋아요 API 를 요청
      {},
      {
        headers: { Authorization: token },
      }
    );
    if (res && res?.data) {
      //카운트증가부분
      setLike((prev) =>
        prev.map((item, idx) => {
          if (idx === index) {
            // item.likeCnt = res.data.likeCnt;
            return res.data.likeCnt;
          }
          return item;
        })
      );
      setIsLike((prev) => !prev); // 좋아요 상태 변경
    }
  };

  return (
    <SWrapper>
      <Container>
        {data.map((post, index) => (
          <PostBoxOne key={post.boardId}>
            <div className="user-info ">
              <div
                className="user-info left"
                role="presentation"
                onClick={() => {
                  navigate(`/profile/${post.member?.memberId}`);
                }}
              >
                {' '}
                <Avatar size="25px" image={post.member?.profileImageUrl} />
                <div className="user-info name">{post.member?.nickname}</div>
              </div>
              <div className="user-info right">
                <span className="user-info height">
                  {post.member?.height}cm
                </span>
                <span className="user-info weight">
                  {post.member?.weight}kg
                </span>
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
              className="add container"
              role="presentation"
              onClick={() => onClickGood(post.boardId, index)}
            >
              {isLike ? <BsBookmarkHeartFill /> : <BsBookmarkHeart />}
              {/* {like[index] ? <BsBookmarkHeartFill /> : <BsBookmarkHeart />} */}
            </div>
          </PostBoxOne>
        ))}
      </Container>
    </SWrapper>
  );
};
const SWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  /* z-index: 300; */
`;
const Container = styled.div`
  display: grid;
  grid-template-columns: 320px 320px 320px;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      grid-template-columns: 320px 320px;
    }
  }
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    & {
      grid-template-columns: 70vw;
    }
  }
`;
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

export default PostBox;
