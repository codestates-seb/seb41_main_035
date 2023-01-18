import styled from 'styled-components';
import { BsChatLeftText, BsPersonPlus, BsBookmarkHeart } from 'react-icons/bs';
import Comment from '../components/Comment';
import Avatar from '../components/Avatar';
import dummyData from '../db/dummyData.json';
import Item from '../components/Item';
import { useParams } from 'react-router-dom';
const BREAK_POINT_PC = 1300;
const BREAK_POINT_TABLET = 768;

const PostView = () => {
  const data = dummyData.posts;
  const params = useParams();
  console.log(params);
  return (
    <SWrapper>
      <SContainer>
        <div className="top_container">
          <SPost>
            <Avatar
              className="outfit_upload"
              size="400px"
              image={data[params.id].picture}
            />
          </SPost>
          <SMiddle>
            <div className="user_info">
              <div className="user_box">
                <div className="user_box left">
                  <div className="user_avatar">
                    <Avatar image={data[params.id].avatar} />
                  </div>
                  <div className="user_info_detail">
                    <div className="user_id">
                      {data[params.id].userNickname}
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
            <div className="post">{data[0].content}</div>
          </SMiddle>
        </div>
        <SBottom>
          <Item />
          <Comment />
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
  width: 50vw;
  height: 53vw;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 650px;
      height: 700px;
    }
  }
  border: 1px solid gray;
  margin: 40px 0px;

  .top_container {
    display: flex;
    margin: 30px 30px 0px 30px;
  }
`;

const SPost = styled.div`
  width: 23.5vw;
  height: 26vw;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 305px;
      height: 340px;
    }
  }
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
  width: 22vw;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      width: 275px;
    }
  }
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
    height: 25vh;
    /* background-color: #eaebba; */
    margin-left: 10px;
    font-size: 20px;
  }
`;
const SBottom = styled.div`
  margin: 15px 30px;
  height: 40%;
`;
export default PostView;
