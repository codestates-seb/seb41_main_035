import styled from 'styled-components';
import { CloseOutlined } from '@ant-design/icons';
import { useEffect, useState } from 'react';
import Avatar from '../components/Avatar';
import axios from 'axios';

const backendUrl = 'http://13.125.30.88/';

function FollowModal(props) {
  const [followData, setFollowData] = useState([]);

  const closeButton = () => {
    // eslint-disable-next-line react/prop-types
    props.onClose();
  };

  useEffect(() => {
    // eslint-disable-next-line react/prop-types
    const user = props.user;
    console.log(user);
    const getFollow = async () => {
      try {
        const token = localStorage.getItem('accessToken');
        // eslint-disable-next-line react/prop-types
        const follwurl = props.isFollow
          ? `${backendUrl}members?tab=followee&page=1&size=10000&memberId=${user}`
          : `${backendUrl}members?tab=follower&page=1&size=10000&memberId=${user}`;
        const res = await axios.get(
          follwurl,
          {
            headers: { Authorization: token },
          },
          { withCredentials: true }
        );
        if (res) {
          setFollowData(res.data.data);
        }
      } catch (err) {
        console.log(err);
      }
      // axios 내정보받아오기
    };
    getFollow();
  }, []);

  return (
    <Overlay>
      <ModalWrap>
        <CloseOutlined
          style={{ 'margin-left': '85%', 'margin-top': '5%' }}
          onClick={closeButton}
        />
        <Contents>
          <div className="comment_container">
            {followData &&
              followData?.map((follow) => (
                <div className="comment_box" key={follow.memberId}>
                  <div className="comment-left">
                    <div className="user_avatar">
                      <Avatar size={'30px'} image={follow.profileImageUrl} />
                    </div>
                    <button
                      className="user_name"
                      onClick={() =>
                        (window.location.href = `/profile/${follow.memberId}`)
                      }
                    >
                      {follow.nickname}
                    </button>
                  </div>
                </div>
              ))}
          </div>
        </Contents>
      </ModalWrap>
    </Overlay>
  );
}

const Overlay = styled.div`
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.2);
  z-index: 9999;
  text-align: center;
`;

const ModalWrap = styled.div`
  width: 400px;
  height: 500px;
  border-radius: 15px;
  background-color: #fff;
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const Contents = styled.div`
  margin: 50px 30px;
  h1 {
    font-size: 30px;
    font-weight: 600;
    margin-bottom: 60px;
  }
  img {
    margin-top: 60px;
    width: 300px;
  }

  .comment_container {
    height: 30vh;

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
    span {
      :hover {
        color: black;
        cursor: pointer;
      }
      .comment_content {
        margin-top: 5px;
        font-size: 14px;
        width: 26vw;
        height: 100%;
      }
    }
  }
`;

export default FollowModal;
