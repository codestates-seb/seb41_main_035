/* eslint-disable react/prop-types */
import styled from 'styled-components';
import {
  BsChatLeftText,
  BsPersonPlus,
  BsPersonCheck,
  BsBookmarkHeart,
} from 'react-icons/bs';
import ChatWindow from '../components/ChatWindow';
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
  const [isUserChatOpen, setIsUserChatOpen] = useState(false);
  // const onChatOpen = () => {
  //   setIsUserChatOpen(true);
  // };
  const url = 'http://13.125.30.88';
  const name = detailData.member?.nickname;
  const sentId = detailData.member?.memberId;
  const boardId = detailData.boardId;
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(url + `/boards/` + [params.boardId]);
        setDetailData(response.data);
        console.log(response.data.products);
      } catch (err) {
        return err;
      }
    };
    fetchData();
  }, []);
  const onPostDelete = () => {
    if (window.confirm('삭제 하시겠습니까?')) {
      axios(url + `/boards/${params.boardId}`, {
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
      `/members/follow?op=${detailData.memberId}&type=down`,
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
      `/members/follow?op=${detailData.memberId}&type=up`,
      {},
      {
        headers: { Authorization: token },
      }
    );
    if (res) {
      setIsFollowing(true);
    }
  };

  const [isChatOpen, setIsChatOpen] = useState(false);
  const onChatOpen = () => {
    setIsChatOpen((prev) => !prev);
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
                    // role="presentation"
                    // onClick={() => {
                    //   navigate(`/profile/${post.member?.memberId}`);
                    // }}
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
                    <BsChatLeftText size="20" onClick={onChatOpen} />
                    {isFollowing ? (
                      <BsPersonCheck size="20" onClick={unfollow} />
                    ) : (
                      <BsPersonPlus size="20" onClick={follow} />
                    )}
                    <BsBookmarkHeart size="20" />
                  </div>
                </div>
              </div>
              <div className="post">{detailData.content}</div>
              <div className="edit-delete">
                <span>Edit</span>
                <span role="presentation" onClick={onPostDelete}>
                  Delete
                </span>
              </div>
            </SMiddle>
          </div>
          <SBottom>
            <Item />
            <Comment name={name} boardId={boardId} />
          </SBottom>
        </SContainer>
      </div>
      {isUserChatOpen && <ChatWindow sentId={sentId} name={name} />}
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
  width: 45vw;
  height: 750px;
  border: 1px solid #b3b3b3;
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

  .top_container {
    display: flex;
    margin: 30px 30px 0px 30px;
  }
`;

const SPost = styled.div`
  width: 20vw;
  height: 335px;
  object-fit: cover;
  position: relative;
  overflow: hidden;
  /* margin-top: 10px; */
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 235px;
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
  width: 27vw;
  /* width: 70%; */
  margin-left: 10px;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 270px;
    }
  }
  .user_info {
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
    height: 280px;
    margin-left: 10px;
    font-size: 20px;
  }
  .edit-delete {
    margin: 5px;
    text-align: right;
    span {
      margin-right: 10px;
      cursor: pointer;
    }
  }
`;
const SBottom = styled.div`
  margin: 15px 30px;
`;
export default PostView;
