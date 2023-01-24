/* eslint-disable react/prop-types */
import styled from 'styled-components';
import { BsChatLeftText, BsPersonPlus, BsBookmarkHeart } from 'react-icons/bs';
import Comment from '../components/Comment';
import Avatar from '../components/Avatar';
import Item from '../components/Item';
import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { token, BREAK_POINT_PC } from '../constants/index';

const PostView = () => {
  const params = useParams();
  const [detailData, setDetailData] = useState([]);
  const url = 'http://13.125.30.88';
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(url + `/boards/` + [params.boardId]);
        setDetailData(response.data);
        console.log(response.data);
      } catch (err) {
        window.alert('오류가 발생했습니다.');
        return err;
      }
    };
    fetchData();
  }, []);
  const onPostDelete = (url) => {
    if (window.confirm('삭제 하시겠습니까?')) {
      axios(url + `/boards/` + [params.boardId], {
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
                  <div className="user_box left">
                    <div className="user_avatar">
                      <Avatar image={detailData.member?.profileImageUrl} />
                    </div>
                    <div className="user_info_detail">
                      <div className="user_id">
                        {detailData.member?.nickname}
                      </div>
                      <div className="user_boxtwo">
                        <div className="user_tall">170cm</div>
                        <div className="user_weight"> 55kg</div>
                      </div>
                    </div>
                  </div>
                  <div className="icon">
                    <BsChatLeftText size="20" />
                    <BsPersonPlus size="20" />
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
            <Comment />
          </SBottom>
        </SContainer>
      </div>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  display: flex;
  justify-content: center;
  .container {
    display: flex;
    justify-content: flex-start;
    left: 0;
    right: 0;
    top: 0;
  }
`;

const SContainer = styled.div`
  width: 47vw;
  height: 750px;
  border: 1px solid gray;
  margin: 60px 30px;
  max-width: 820px;
  background-color: #f4f1e0;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 650px;
      height: 700px;
    }
  }

  .top_container {
    display: flex;
    margin: 30px 30px 0px 30px;
  }
`;

const SPost = styled.div`
  width: 22.5vw;
  height: 320px;
  object-fit: cover;
  position: relative;
  overflow: hidden;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 290px;
      height: 320px;
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
  width: 22vw;
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
    margin: 5px;
    align-items: center;
    justify-content: space-between;
    .user_id {
      margin-left: 10px;
      font-size: 18px;
    }
    svg {
      padding-left: 10px;
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
    height: 28vh;
    margin-left: 10px;
    font-size: 20px;
  }
  .edit-delete {
    margin: 5px;
    text-align: right;
    span {
      margin-right: 10px;
    }
  }
`;
const SBottom = styled.div`
  margin: 15px 30px;
`;
export default PostView;
