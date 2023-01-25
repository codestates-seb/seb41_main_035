import LoginHeader from '../components/LoginHeader';
import styled from 'styled-components';
import PostBox from '../components/PostBox';
import Sidebar from '../components/Sidebar';
import UserInfo from '../components/UserInfo';
import userStore from '../store/userStore';
import { useParams } from 'react-router-dom';

const Profile = () => {
  const params = useParams();
  const userId = params.userId;
  const userStoreId = userStore((state) => state.userId);

  return (
    <>
      <SWrapper>
        <Sidebar />
        <div className="main post">
          <UserInfo />
          <Sline></Sline>
          {userId == userStoreId ? (
            <Filter>
              <SCodi>
                <SMyCodi>My Codi</SMyCodi>
                <SLikeCodi>Like Codi</SLikeCodi>
              </SCodi>
            </Filter>
          ) : (
            ''
          )}
          {/* <PostBox /> */}
        </div>
      </SWrapper>
    </>
  );
};
const SWrapper = styled.div`
  display: flex;
`;
const Filter = styled.div`
  display: flex;
  justify-content: center;
  height: 150px;
  button {
    margin: 50px;
    font-size: 30px;
    width: 170px;
    height: 40px;
    background-color: #ece9ca;
  }
`;

const SMyCodi = styled.button`
  margin: 30px;
  font-size: 30px;
  width: 150px;
  height: 50px;
  margin-right: 100px;
  margin-left: 100px;
  border-radius: 20px;
  justify-content: center;
  display: flex;
`;

const SLikeCodi = styled.button`
  margin: 30px;
  font-size: 30px;
  width: 150px;
  height: 50px;
  border-radius: 20px;
  justify-content: center;
  display: flex;
`;

const SCodi = styled.div`
  display: flex;
  justify-content: center;
`;

const Sline = styled.hr`
  display: flex;
  justify-content: center;
`;

export default Profile;
