import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
const BREAK_POINT_TABLET = 768;
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
                usenavigate(`/category/outer`);
              }}
            >
              아우터
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/top`);
              }}
            >
              상의
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/bottom`);
              }}
            >
              하의
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/onepiece`);
              }}
            >
              원피스
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/hat`);
              }}
            >
              모자
            </p>
            <p
              className="items"
              role="presentation"
              onClick={() => {
                usenavigate(`/category/shoes`);
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
  /* overflow: hidden; */
  display: flex;
  width: 18%;
  justify-content: flex-end;
  /* text-align: center; */
  /* margin-right: auto; */
`;

const SidebarBox = styled.div`
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #4e4e4e;
    padding: 40px 0px 20px 0px;
    font-family: 'Gowun Batang', serif;
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
