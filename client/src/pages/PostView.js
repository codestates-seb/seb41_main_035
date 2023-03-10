/* eslint-disable react/prop-types */
import styled from 'styled-components';
import {
  BsChatLeftText,
  BsPersonPlus,
  BsPersonCheck,
  BsBookmarkHeart,
  BsBookmarkHeartFill,
} from 'react-icons/bs';
import Comment from '../components/Comment';
import Avatar from '../components/Avatar';
import Item from '../components/Item';
import { useParams, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { token, BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';

const PostView = () => {
  const navigate = useNavigate();
  const params = useParams();
  const [detailData, setDetailData] = useState([]);
  const [isFollowing, setIsFollowing] = useState(false);
  const API_URL = process.env.REACT_APP_API_URL;
  const name = detailData.member?.nickname;
  const sentId = detailData.member?.memberId;
  const boardId = detailData.boardId;
  const profile = detailData.member?.profileImageUrl;
  localStorage.setItem('sentId', JSON.stringify(sentId));
  localStorage.setItem('name', JSON.stringify(name));
  localStorage.setItem('boardId', JSON.stringify(boardId));
  localStorage.setItem('profile', JSON.stringify(profile));

  //좋아요부분
  const onClickGood = async (id) => {
    const token = localStorage.getItem('accessToken');
    const res = await axios.post(
      `${API_URL}boards/${id}/like`, // 좋아요 API
      {},
      {
        headers: { Authorization: token },
      }
    );
    if (res && res?.data) {
      setDetailData((prev) => {
        return { ...prev, like: !prev.like };
      });
    }
  };
  useEffect(() => {
    const fetchData = async () => {
      const token = localStorage.getItem('accessToken');
      try {
        const response = await axios.get(
          API_URL + `boards/` + [params.boardId],
          {
            headers: { Authorization: token },
          }
        );
        setDetailData(response.data);
        setIsFollowing(response.data.member.follow);
      } catch (err) {
        return err;
      }
    };
    fetchData();
  }, []);
  const onPostDelete = () => {
    if (window.confirm('삭제 하시겠습니까?')) {
      axios(API_URL + `boards/${params.boardId}`, {
        method: 'DELETE',
        headers: {
          Authorization: token,
        },
      })
        .then((res) => {
          if (res) {
            location.href = '/';
          }
        })
        .catch((err) => {
          return err;
        });
    }
  };
  const unfollow = async () => {
    const token = localStorage.getItem('accessToken');
    const res = await axios.post(
      API_URL + `members/follow?op=${detailData.member.memberId}&type=down`,
      {},
      {
        headers: { Authorization: token },
      }
    );
    if (res) {
      setIsFollowing(false);
    }
  };

  const follow = async () => {
    const token = localStorage.getItem('accessToken');
    const res = await axios.post(
      API_URL + `members/follow?op=${detailData.member.memberId}&type=up`,
      {},
      {
        headers: { Authorization: token },
      }
    );
    if (res) {
      setIsFollowing(true);
    }
  };

  return (
    <SWrapper>
      <div className="container">
        <SContainer>
          <div className="top_container">
            <SPost>
              <Avatar
                className="outfit_upload"
                size="400px"
                image={detailData.userImage}
              />
            </SPost>
            <SMiddle>
              <div className="user_info">
                <div className="user_box">
                  <div
                    className="user_box left"
                    role="presentation"
                    onClick={() => {
                      navigate(`/profile/${sentId}`);
                    }}
                  >
                    <div className="user_avatar">
                      <Avatar image={detailData.member?.profileImageUrl} />
                    </div>
                    <div className="user_info_detail">
                      <div className="user_id">
                        {detailData.member?.nickname}
                      </div>
                      <div className="user_boxtwo">
                        <div className="user_tall">
                          {detailData.member?.height}cm
                        </div>
                        <div className="user_weight">
                          {' '}
                          {detailData.member?.weight}kg
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="icon">
                    <BsChatLeftText
                      size="20"
                      onClick={() => {
                        navigate(`/chatting`);
                      }}
                    />
                    {isFollowing ? (
                      <BsPersonCheck size="20" onClick={unfollow} />
                    ) : (
                      <BsPersonPlus size="20" onClick={follow} />
                    )}
                    {/* 좋아요기능 */}
                    <div
                      className="container add"
                      role="presentation"
                      onClick={() => onClickGood(detailData.boardId)}
                    >
                      {detailData.like ? (
                        <BsBookmarkHeartFill size="20" />
                      ) : (
                        <BsBookmarkHeart size="20" />
                      )}
                    </div>
                  </div>
                </div>
              </div>
              <div className="post">{detailData.content}</div>
              <div className="edit-delete">
                <span role="presentation" onClick={onPostDelete}>
                  Delete
                </span>
              </div>
            </SMiddle>
          </div>
          <span className="products">착용 제품</span>
          <SBottom>
            <Item />
            <Comment name={name} boardId={boardId} profile={profile} />
          </SBottom>
        </SContainer>
      </div>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  display: flex;
  justify-content: center;
  margin-left: 200px;
  min-width: 500px;
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    margin-left: 12px; // 카테고리 사이드바가 있으면 이게 그나마 최선..
  }

  .container {
    display: flex;
    justify-content: flex-start;
    left: 0;
    right: 0;
    top: 0;
  }
`;

const SContainer = styled.div`
  font-family: 'Gothic A1', sans-serif;
  width: 45vw;
  height: 750px;
  border: 1px solid lightgray;
  border-radius: 8px;
  margin: 60px 30px;
  max-width: 820px;
  background-color: white;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 564px;
      /* height: 700px; */
    }
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      width: 550px;
    }
  }
  .products {
    margin-left: 40px;
    color: black;
    font-size: 14px;
  }
  .top_container {
    display: flex;
    margin: 30px 30px 10px 30px;
  }
`;

const SPost = styled.div`
  width: 25vw;
  height: 335px;
  object-fit: cover;
  position: relative;
  overflow: hidden;
  /* margin-top: 10px; */
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 270px;
      height: 330px;
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
`;

const SMiddle = styled.div`
  width: 24vw;
  /* width: 70%; */
  margin-left: 10px;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 235px;
    }
  }
  .user_info {
    cursor: pointer;
    display: flex;
    flex-direction: column;
  }
  .user_box {
    display: flex;
    margin: 0px 5px 5px 5px;
    align-items: center;
    justify-content: space-between;
    .user_id {
      margin-left: 10px;
      font-size: 18px;
    }
    svg {
      :hover {
        color: gray;
      }
      padding-left: 15px;
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
  .user_boxtwo {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 10px;
    .user_tall {
      margin: 0px 10px;
    }
  }
  .icon {
    cursor: pointer;
    display: flex;
  }
  .post {
    height: 260px;
    margin-left: 10px;
    font-size: 20px;
    white-space: pre-wrap;
  }
  .edit-delete {
    margin: 5px;
    text-align: right;

    span {
      margin-right: 10px;
      cursor: pointer;
      :hover {
        color: gray;
      }
    }
  }
`;
const SBottom = styled.div`
  margin: 15px 30px;
`;
export default PostView;
