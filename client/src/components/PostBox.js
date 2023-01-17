import styled from 'styled-components';
import dummyData from '../db/dummyData.json';
import Avatar from '../components/Avatar';
import { BsBookmarkHeart } from 'react-icons/bs';
import { useNavigate } from 'react-router-dom';
const BREAK_POINT_PC = 1300;
const BREAK_POINT_TABLET = 768;
const PostBox = () => {
  const navigate = useNavigate();
  return (
    <SWrapper>
      <Container>
        {dummyData.posts.map((post) => (
          <PostBoxOne
            key={post.id}
            onClick={() => {
              navigate(`/postview`);
            }}
          >
            <div className="user-info ">
              <div className="user-info left">
                {' '}
                <Avatar size="25px" image={post.avatar} />
                <div className="user-info name">{post.userNickname}</div>
              </div>
              <div className="user-info right">
                <div className="user-info height">170cm</div>
                <div className="user-info weight">56kg</div>
              </div>
            </div>

            <div className="style-picture">
              <Avatar size="400px" image={post.picture} />
            </div>
            <div className="add container">
              <BsBookmarkHeart />
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
`;
const Container = styled.div`
  display: grid;
  grid-template-columns: 22vw 22vw 22vw;
  /* grid-template-columns: 320px 320px 320px; */
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    & {
      grid-template-columns: 320px 320px;
    }
  }
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    & {
      grid-template-columns: 320px;
    }
  }
`;
const PostBoxOne = styled.div`
  height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0px 15px 60px 15px;
  border: 2px solid #949494;
  border-radius: 5px;
  cursor: pointer;
  .user-info {
    display: flex;
    width: 90%;
    padding-top: 8px;
    padding-bottom: 4px;
    align-items: center;
    justify-content: space-between;
    .right {
      color: #2e2d2a;
      font-size: 15px;
    }
    .name {
      margin-left: 10px;
      margin-bottom: 7px;
      font-size: 20px;
    }
  }
  .style-picture {
    width: 18vw;
    height: 300px;
    object-fit: cover;
    position: relative;
    overflow: hidden;
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      & {
        width: 258px;
      }
    }
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      & {
        width: 258px;
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
    width: 95%;
    justify-content: flex-end;
    margin-top: 8px;
    svg {
      font-size: 20px;
    }
  }
`;

export default PostBox;
