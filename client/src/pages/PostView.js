import styled from 'styled-components';
import {
  BsChatLeftText,
  BsPersonPlus,
  BsPersonCheck,
  BsBookmarkHeart,
} from 'react-icons/bs';
// import Comment from '../components/Comment';
import Avatar from '../components/Avatar';
import dummyData from '../db/dummyData.json';
import Item from '../components/Item';

import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
const BREAK_POINT_PC = 1300;

const PostView = () => {
  const data = dummyData.posts;
  const params = useParams();
  console.log(params);
  const [detailData, setDetailData] = useState([]);
  const [isFollowing, setIsFollowing] = useState(false);
  const url = 'http://13.125.30.88/comment';

  // useEffect(() => {
  //   const fetchData = async () => {
  //     try {
  //       //postId로 변경
  //       const response = await axios.get(url + `/post/` + [params.id]);

  //       setDetailData(response.data);
  //       console.log(response.data);

  //       if (response.data.is_following) {
  //         setIsFollowing(true);
  //       }
  //     } catch (err) {
  //       window.alert('오류가 발생했습니다.');
  //       return err;
  //     }
  //   };
  //   fetchData();
  // }, []);

  const unfollow = async () => {
    const token = localStorage.getItem('accessToken');
    const res = await axios.post(
      `/members/follow?op=${detailData.memberId}&type=down`,
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

  return (
    <SWrapper>
      <SContainer>
        <div className="top_container">
          <SPost>
            <Avatar
              className="outfit_upload"
              size="400px"
              image={data[0].picture}
            />
          </SPost>
          <SMiddle>
            <div className="user_info">
              <div className="user_box">
                <div className="user_box left">
                  <div className="user_avatar">
                    <Avatar image={data[0].avatar} />
                  </div>
                  <div className="user_info_detail">
                    <div className="user_id">{data[0].userNickname}</div>
                    <div className="user_boxtwo">
                      <div className="user_tall">170cm</div>
                      <div className="user_weight"> 55kg</div>
                    </div>
                  </div>
                </div>
                <div className="icon">
                  <BsChatLeftText size="20" />
                  {isFollowing ? (
                    <BsPersonCheck size="20" onClick={unfollow} />
                  ) : (
                    <BsPersonPlus size="20" onClick={follow} />
                  )}
                  <BsBookmarkHeart size="20" />
                </div>
              </div>
            </div>
            <div className="post">{data[0].content}</div>
          </SMiddle>
        </div>
        <SBottom>
          <Item />
          {/* <Comment /> */}
        </SBottom>
      </SContainer>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;
`;

const SContainer = styled.div`
  width: 650px;
  height: 660px;
  border: 1px solid gray;
  margin: 40px 0px;

  .top_container {
    display: flex;
    margin: 30px 30px 0px 30px;
  }
`;

const SPost = styled.div`
  width: 300px;
  height: 300px;
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
`;

const SMiddle = styled.div`
  width: 270px;
  margin-left: 10px;
  .user_info {
    display: flex;
    flex-direction: column;
  }
  .user_box {
    display: flex;
    margin: 5px;
    align-items: center;
    justify-content: space-between;
    .user_id {
      margin-left: 10px;
      font-size: 18px;
    }
    svg {
      padding-left: 5px;
    }
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
  .user_boxtwo {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 10px;
    .user_tall {
      margin: 0px 10px;
    }
  }
  .post {
    height: 25vh;
    margin-left: 10px;
    font-size: 20px;
  }
`;
const SBottom = styled.div`
  margin: 15px 30px;
`;
export default PostView;
