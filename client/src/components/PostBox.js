import styled from 'styled-components';
import dummyData from '../db/dummyData.json';
import Avatar from '../components/Avatar';
import { BsBookmarkHeartFill } from 'react-icons/bs';
const PostBox = () => {
  return (
    <SWrapper>
      <Container>
        {dummyData.posts.map((post) => (
          <PostBoxOne key={post.id}>
            <div className="user-info ">
              <Avatar size="25px" image={post.avatar} />
              <div className="user-info name">{post.userNickname}</div>
            </div>

            <div className="style-picture">
              <Avatar size="400px" image={post.picture} />
            </div>
            <div className="add container">
              <BsBookmarkHeartFill />
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
`;
const Container = styled.div`
  width: 70%;
  display: flex;
  flex-wrap: wrap;
`;
const PostBoxOne = styled.div`
  width: 30%;
  height: 400px;
  background-color: #d9d9d9;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-wrap: wrap;
  margin: 30px 15px;

  .user-info {
    display: flex;
    width: 80%;
    padding-top: 8px;
    padding-bottom: 4px;
    align-items: center;
    .name {
      margin-left: 10px;
      margin-bottom: 7px;
    }
  }
  .style-picture {
    width: 250px;
    height: 300px;
    background-color: skyblue;
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
  .add {
    display: flex;
    width: 80%;
    justify-content: flex-end;
    margin-top: 10px;
    svg {
      font-size: 20px;
    }
  }
`;

export default PostBox;
