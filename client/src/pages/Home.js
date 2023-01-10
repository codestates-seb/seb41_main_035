import styled from 'styled-components';
import LoginHeader from '../components/LoginHeader';
// import LogoutHeader from '../components/LogoutHeader';
import PostBox from '../components/PostBox';
const Home = () => {
  return (
    <>
      <LoginHeader />
      {/* <LogoutHeader /> */}
      <Filter>
        <button className="filter button">Hot</button>
        <button className="filter button">New</button>
        <button className="filter button">렌탈</button>
        <button className="filter button">Follow</button>
      </Filter>
      <PostBox />
    </>
  );
};
const Filter = styled.div`
  display: flex;
  justify-content: center;
  height: 150px;
  button {
    margin: 30px;
    font-size: 30px;
    width: 130px;
    height: 50px;
  }
`;

export default Home;
