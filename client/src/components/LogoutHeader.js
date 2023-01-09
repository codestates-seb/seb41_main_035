import styled from 'styled-components';
import { BsPersonCircle, BsPencilSquare } from 'react-icons/bs';
import { AiOutlineMessage } from 'react-icons/ai';
const LogoutHeader = () => {
  return (
    <>
      <SWrapper>
        <SHeader>
          <p className="title">Look at me</p>
          <form>
            <input
              className="header-bottom-search"
              type="search"
              placeholder="브랜드명, 상품명으로 검색"
            ></input>
          </form>
          <BsPersonCircle size="30" />
          <BsPencilSquare size="30" />
          <AiOutlineMessage size="30" />
          <button className="login button">로그아웃</button>
        </SHeader>
      </SWrapper>
    </>
  );
};
const SWrapper = styled.div`
  position: sticky;
  top: 0;
  z-index: 5;
  justify-content: center;
  display: flex;
`;
const SHeader = styled.div`
  background-color: #d9d9d9;
  width: 70%;
  height: 15vh;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding-top: 20px;

  .title {
    font-size: 60px;
    margin-left: -20px;
  }
  input {
    width: 25vw;
    height: 5vh;
    border: 2px solid black;
    margin-top: 15px;
    margin-left: -70px;
    background: transparent;
  }
  button {
    width: 5vw;
    height: 30px;
    margin-left: -80px;
    margin-top: 15px;
    background-color: #f67b7b;
    color: white;
  }
  svg {
    margin-left: -80px;
    margin-top: 15px;
  }
`;

export default LogoutHeader;
