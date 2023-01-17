import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';

const Sidebar = () => {
  const usenavigate = useNavigate();

  return (
    <SWrapper>
      <SidebarBox>
        <div>
          <p className="title">카테고리</p>
        </div>
        <ItemList>
          <div>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/outer`);
              }}
            >
              아우터
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/top`);
              }}
            >
              상의
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/bottom`);
              }}
            >
              하의
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/onepiece`);
              }}
            >
              원피스
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/hat`);
              }}
            >
              모자
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/shoes`);
              }}
            >
              신발
            </p>
          </div>
        </ItemList>
      </SidebarBox>
    </SWrapper>
  );
};

//전체부분
const SWrapper = styled.div`
  position: fixed;
  overflow: hidden;
  display: flex;
  width: 10%;
  justify-content: center;
  /* text-align: center; */
  /* margin-right: auto; */
`;

const SidebarBox = styled.div`
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #4e4e4e;
    padding: 40px 0px 20px 0px;
  }
`;
const ItemList = styled.div`
  .items {
    font-size: 15px;
    cursor: pointer;
    padding: 10px 0px;
    :hover {
      color: #196ba5;
    }
  }
`;
export default Sidebar;
